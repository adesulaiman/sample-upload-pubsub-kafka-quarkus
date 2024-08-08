# Flow Process
## JWT Auth RBAC
endpoint login [http://localhost:8080/process/login] use to get token for RBAC admin in upload endpoint[ http://localhost:8080/process/uploadfile]
![Alt text](auth_login.png)


## Process bulk / batch upload
endpoint bulk upload [http://localhost:8080/process/bulkupload] use to upload multiple files
![Alt text](multiple_upload.png)


## Queue Processing with Kafka
When data uploaded, csv and xls will convert to json structure then send to kafka massage to process queue in background
![Alt text](queue_process.png)

## Final process data will store to table
End of process record will be store to table in pgsql
![Alt text](store_to_table.png)
