package app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class UnzipUtility {

    private static final int S_BYTE_SIZE = 4096;

    public static List<String> downloadAndUnzip(String urlString, String zipFilePath,
            String destDirectory)
    throws IOException {

        URL tariff = new URL(urlString);
        String myUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";

        java.net.URLConnection c = tariff.openConnection();
        c.setRequestProperty("User-Agent", myUserAgent);

        ReadableByteChannel zipByteChannel = Channels.newChannel(c.getInputStream());
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        fos.getChannel().transferFrom(zipByteChannel, 0, Long.MAX_VALUE);

        fos.close();

        return unzip(zipFilePath, destDirectory);

    }

    public static List<String> unzip(String zipFilePath, String destDirectory) throws IOException {

        List<String> unzippedFileList = new ArrayList<>();

        File destDir = new File(destDirectory);

        if(!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream((zipFilePath)));

        ZipEntry zipEntry = zipIn.getNextEntry();

        while (zipEntry != null) {
            String filePath = destDirectory + File.separator + zipEntry.getName();

            if (!zipEntry.isDirectory()) {
                String oneUnzippedFile = extractFile(zipIn, filePath);
                unzippedFileList.add(oneUnzippedFile); 
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipEntry = zipIn.getNextEntry();
        }
        zipIn.close();

        return unzippedFileList;

    }

    private static String extractFile(ZipInputStream zipIn, String filePath) throws IOException {

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));

        byte[] bytesIn = new byte[S_BYTE_SIZE];

        int read = 0;

        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        return filePath;

    }
}