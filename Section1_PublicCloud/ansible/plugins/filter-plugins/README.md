# Collection of Ansible filters I have written.

Every filter I write will contain information in this README.md, that shows the various filters and a basic example of each one. I will blog about each one that I post here and how I use them.

# AWS Filter Plugins
* Place these python files into the filter_plugins directory in the root of your playbook directory. If your playbooks live in ```/etc/ansible/playbooks```, than it should look like this ```/etc/ansible/playbooks/filter_plugins/aws.py```


# AWS.py 

In order to use these plugins please install boto, boto3, botocore, and awsretry
`pip install boto, boto3, botocore, awsretry` or `pip install -r requirements.txt`

## Each aws filter is surrounded by the aws_retry decorator.
The AWSRetry.backoff() decorator will retry based on the following exceptions
* RequestLimitExceeded
* Unavailable
* ServiceUnavailable
* InternalFailure
* InternalError
* "\w+.NotFound" (Eventual Consistency)

The AWSRetry.backoff() decorator takes on the following kwargs.
* tries (number of times to try) default=10
* delay (initial delay between retries in seconds) default=3
* backoff (This is the multiplier, that will double on each retry) default=2

## Ansible Filters for AWS in aws.py
* get_vpc_id_by_name
* get_ami_image_id
* get_instance_id_by_name
* get_subnet_ids
* get_sg
* get_sg_cidrs
* get_older_images
* get_instance
* get_all_vpcs_info_except
* get_route_table_ids
* get_all_route_table_ids
* get_all_route_table_ids_except
* get_subnet_ids_in_zone
* latest_ami_id
* get_rds_endpoint
* zones
* get_sqs
* get_instance_profile
* get_server_certificate
* vpc_exists
* get_dynamodb_base_arn
* get_kinesis_stream_arn
* get_account_id
* get_instance_by_tags
* get_instances_by_tags
* get_acm_arn
* get_redshift_ip
* get_redshift_endpoint
* get_elasticache_endpoint
* get_vpc_ids_from_names
* get_route53_id
* get_instance_tag_name_by_ip
* get_sg_subnets
* get_role_arn
* get_instances_attr_by_ids
* get_instance_ips_in_asg
* get_cloudfront_dns

## AWS Examples.
Example placement of the filters below *roles/vpc_deploy/vars/main.yml*
```yaml
---
aws_region: us-west-2
zone_2c: us-west-2c
vpc_name: test
vpc_id: "{{ vpc_name | get_vpc_id_by_name(aws_region) }}"
subnet_ids_in_west_2c: "{{ vpc_id | get_subnet_ids_in_zone(zone, aws_region)}}"
```
