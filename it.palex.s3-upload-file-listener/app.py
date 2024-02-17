import os
import boto3

s3_client = boto3.client('s3')
dynamodb_client = boto3.client('dynamodb')

# Get DynamoDB table name from environment variable
dynamodb_table_name = os.environ.get('DYNAMODB_TABLE_NAME')
if not dynamodb_table_name:
    raise ValueError("DynamoDB table name not found in environment variables.")

valid_extensions_str = os.environ.get('VALID_IMAGE_EXTENSIONS', '')
if not valid_extensions_str:
    raise ValueError("Valid image extensions not found in environment variables.")

valid_extensions = valid_extensions_str.split(',')


clean_image_folder = os.environ.get('CLEAN_IMAGE_FOLDER', '')
if not clean_image_folder:
    raise ValueError("Clean image folder not found in environment variables.")


max_image_size = os.environ.get('MAX_IMAGE_SIZE', '')
max_image_size = int(max_image_size)


def lambda_handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key']
        size = record['s3']['object']['size']
        
        # Extract movie identifier from object key
        movie_identifier = extract_movie_identifier(key)        

        # Check if the file is valid or needs to be deleted
        if not check_image_file(size):
            print(f"File {key} is not a valid image. Deleting...")
            delete_file(bucket, key)
            
            return
        
        # Extract filename from object key
        filename = os.path.basename(key)
        
        # Move the image to the clean directory
        clean_key = os.path.join(clean_image_folder, filename)
        move_image(bucket, key, clean_key)

        # Update database entry for the movie
        if movie_identifier:
            update_database(movie_identifier, clean_key)
        else:
            print(f"Invalid key format: {key}. Skipping database update.")

def extract_movie_identifier(key):
    filename = os.path.basename(key)
    parts = os.path.splitext(filename)
    
    # Ensure there is at least one part before the file extension
    if len(parts) >= 1:
        # Get the part before the last '.' as movie identifier
        movie_identifier = parts[0]
        
        # Check if the movie identifier is not empty
        if movie_identifier:
            return movie_identifier
        else:
            print(f"Empty movie identifier found in key: {key}")
            return None
    else:
        print(f"No file extension found in key: {key}")
        return None

def update_database(movie_identifier, key):
    response = dynamodb_client.get_item(
        TableName=dynamodb_table_name,
        Key={'Id': {'S': movie_identifier}}
    )

    if 'Item' in response:
        # Update the database entry to indicate that the movie has a cover image
        dynamodb_client.update_item(
            TableName=dynamodb_table_name,
            Key={'Id': {'S': movie_identifier}},
            UpdateExpression='SET CoverObjectPath = :path',
            ExpressionAttributeValues={':path': {'S': key}}
        )
    else:
        print(f"Item with movie identifier {movie_identifier} does not exist. Skipping update.")


def move_image(bucket, src_key, dst_key):
    # Copy the object to the clean directory
    s3_client.copy_object(
        Bucket=bucket,
        CopySource={'Bucket': bucket, 'Key': src_key},
        Key=dst_key
    )

    # Wait for the copied object to exist
    waiter = s3_client.get_waiter('object_exists')
    waiter.wait(Bucket=bucket, Key=dst_key)

    # Delete the source object
    s3_client.delete_object(
        Bucket=bucket,
        Key=src_key
    )


def check_image_file(object_size):
    if object_size > max_image_size:
        return False
    
    # !!!!!!!!!! IN A REAL ENVIRONMENT(after checking the size) SANITIZE THE IMAGE FILE, compress it or some other things
    return True


def delete_file(bucket, key):
    # Delete the file from S3
    s3_client.delete_object(Bucket=bucket, Key=key)


def get_object_size(bucket, key):
    try:
        response = s3_client.head_object(Bucket=bucket, Key=key)
        return response['ContentLength']
    except Exception as e:
        print(f"Error getting object size for {key}: {e}")
        return None