import services.FileRenamingService

static void main(String[] args) {
    Scanner workingDirectoryScanner = new Scanner(System.in)
    println("Set Working Directory:")
    String workingDirectory = workingDirectoryScanner.nextLine()

    Scanner backupDirectoryScanner = new Scanner(System.in)
    println("Set Backup Directory:")
    String backupDirectory = backupDirectoryScanner.nextLine()

    Scanner fileNameWithScanner = new Scanner(System.in)
    println("File Name with Char Sequence:")
    String fileNameWith = fileNameWithScanner.nextLine()

    Scanner replaceWithScanner = new Scanner(System.in)
    println("Replace Char Sequence with:")
    String replaceWith = replaceWithScanner.nextLine()

    FileRenamingService service = new FileRenamingService(
        workingDirectory,
        backupDirectory,
        fileNameWith,
        replaceWith
    )
    service.execute()
    println('Done.')
}