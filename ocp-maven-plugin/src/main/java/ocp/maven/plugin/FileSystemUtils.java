package ocp.maven.plugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemUtils {
    public static final List<File> getAllFilesFromResource(String folder)
            throws URISyntaxException, IOException {

        URL resource = classLoader().getResource(folder);
        if (resource != null) {
            // dun walk the root path, we will walk all the classes
            List<File> collect = Files.walk(Paths.get(resource.toURI()))
                    .filter(Files::isRegularFile)
                    .map(x -> x.toFile())
                    .collect(Collectors.toList());

            return collect;
        } else {
            return Collections.emptyList();
        }
    }

    public static final List<Path> getPathsFromResourceJAR(String sourceRootClassPath) throws URISyntaxException, IOException {

        List<Path> result;

        // get path of the current running JAR
        String jarPath = FileSystemUtils.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        // file walks JAR
        URI uri = URI.create("jar:file:" + jarPath);
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            result = Files.walk(fs.getPath(sourceRootClassPath))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public static final InputStream getFileFromResourceAsStream(String fileName) {
        InputStream inputStream = classLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    
    public static final void copyDirectoryFromJar(String sourceRootClassPath, String destinationRootDirectory, int destinationPathOffset)
            throws URISyntaxException, IOException {

        // get path of the current running JAR
        String jarPath = FileSystemUtils.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        // file walks JAR
        URI uri = URI.create("jar:file:" + jarPath);
        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            Files.walk(fs.getPath(sourceRootClassPath))
                    .filter(Files::isRegularFile)
                    .forEach(sourceClassFile -> copyFileFromJar(sourceClassFile, destinationRootDirectory, destinationPathOffset));
        }
    }


    public static final void copyDirectory(String sourceRootDirectory, String destinationRootDirectory)
            throws IOException {
        Files.walk(Paths.get(sourceRootDirectory))
                .forEach(sourceFile -> copyFile(sourceFile, sourceRootDirectory, destinationRootDirectory));
    }

    public static final void copyFile(Path sourceFile, String sourceRootDirectory, String destinationRootDirectory) {
        Path destination = Paths.get(destinationRootDirectory, sourceFile.toString().substring(sourceRootDirectory.length()));
        try {

            if (sourceFile.toFile().isDirectory()) {
                if (!destination.toFile().exists()) {
                    destination.toFile().mkdirs();
                }
            } else {
                Files.copy(sourceFile, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final void copyFileFromJar(Path sourceClassFile, String destinationRootDirectory, int destinationPathOffset) {
        try {
            String filePathInJAR = sourceClassFile.toString();
            if (filePathInJAR.startsWith("/")) {
                filePathInJAR = filePathInJAR.substring(1, filePathInJAR.length());
            }
            InputStream is = FileSystemUtils.getFileFromResourceAsStream(filePathInJAR);
            Path targetFilePath = Paths.get(String.format("%s/%s", destinationRootDirectory, destinationPathOffset > 0 ? filePathInJAR.substring(destinationPathOffset) : filePathInJAR));
            if (!targetFilePath.toFile().getParentFile().exists()) {
               targetFilePath.toFile().getParentFile().mkdirs();
            }            
            Files.copy(is, targetFilePath, StandardCopyOption.REPLACE_EXISTING); 


                 
                
                
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static ClassLoader classLoader() {
        return FileSystemUtils.class.getClassLoader();
    }
}
