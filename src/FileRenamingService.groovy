import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.text.MessageFormat
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger

class FileRenamingService {
    private Logger logger
    private FileHandler fileHandler

    // the directory that the service will look into
    private String workingDirectory
    // the directory where the service will backup the files before renaming
    private String backupDirectory
    // the string that the service will look for in the file names
    private String fileNameContains
    // the string that the service will rename the files with
    private String replaceWith

    FileRenamingService(
        String workingDirectory,
        String backupDirectory,
        String fileNameContains,
        String replaceWith,
        String logFilePath = 'event.log'
    ) {
        this.workingDirectory = workingDirectory
        this.backupDirectory = backupDirectory
        this.fileNameContains = fileNameContains
        this.replaceWith = replaceWith

        // configure logging for the service (File + Console Handlers)
        this.fileHandler = new FileHandler(logFilePath)
        this.fileHandler.setLevel(Level.ALL)

        this.logger = Logger.getLogger('service')
        this.logger.addHandler(this.fileHandler)
    }

    void execute() {
        this.createBackupDirectory()
        Files.walk(Paths.get(this.workingDirectory)).forEach {
            this.logger.info(MessageFormat.format('Checking Path: {0}', it))

            // guard clause to check if the path is actually a file
            if (!this.isFile(it)) {
                this.logger.info('Not a file. Skipping...')
                return
            }

            String fileName = this.getFileName(it)

            // guard clause to check if the file name has the sequence we are looking for
            if (!fileName.contains(this.fileNameContains)) {
                this.logger.info('File name does not match char sequence. Skipping...')
                return
            }

            this.backupFile(it)
            String fileChecksum = this.getFileMD5Checksum(it)
            String newFileName = this.generateNewFileName(fileName, fileChecksum)

            // rename the file in place
            this.renameFile(it, newFileName)
        }
    }

    private void createBackupDirectory() {
        if (!Files.isDirectory(Paths.get(this.backupDirectory))) {
            new File(this.backupDirectory).mkdirs()
        }
    }

    private boolean isFile(Path path) {
        return path.toFile().isFile()
    }

    private String getFileName(Path path) {
        return path.getFileName()
    }

    private String getFileMD5Checksum(Path path) {
        def dataBytes = Files.readAllBytes(path)
        def hashBytes = MessageDigest.getInstance('MD5').digest(dataBytes)
        def checksum = new BigInteger(1, hashBytes).toString(16)
        return checksum
    }

    private void backupFile(Path path) {
        Path source = path
        Path target = Paths.get(
            this.backupDirectory,
            MessageFormat.format(
                '{0}-{1}-{2}',
                System.currentTimeMillis().toString(),
                this.getFileMD5Checksum(path),
                this.getFileName(path)
            )
        )
        Files.copy(source, target)
        this.logger.info(
            MessageFormat.format(
                'Backed up {0} to {1}',
                source.toString(),
                target.toString()
            )
        )
    }

    // generate a new file name of pattern: <modified file name>.<file extension>
    private String generateNewFileName(String fileName, String checksum) {
        String newFileName = fileName
        newFileName = newFileName.replace(this.fileNameContains, this.replaceWith)
        this.logger.info(MessageFormat.format('New File Name: {0}', newFileName))
        return newFileName
    }

    private void renameFile(Path file, String newFileName) {
        Path source = file
        Path target = Paths.get(
            source.toString().replace(this.getFileName(source), newFileName)
        )
        Files.move(source, target)
        this.logger.info(
            MessageFormat.format(
                'Renamed {0} to {1} successfully',
                this.getFileName(source),
                this.getFileName(target)
            )
        )
    }
}
