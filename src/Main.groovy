static void main(String[] args) {
    String workingDirectory = 'C:\\Users\\acmacunlay\\Documents\\Test Data'
    String backupDirectory = 'C:\\Users\\acmacunlay\\Documents\\Backup'
    String contains = '0'
    String replaceWith = '9'
    String logFilePath = 'event.log'

    FileRenamingService service = new FileRenamingService(
        workingDirectory,
        backupDirectory,
        contains,
        replaceWith,
        logFilePath
    )
    service.execute()
}