package org.provider.jpfimpl;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtils {
	
		private final static Logger logger = LoggerFactory.getLogger(ZipUtils.class);

        /**
         * Creates a zip file at the specified path with the contents of the
         * specified directory.
         * NB:
         * 
         * @param directoryPath
         *            The path of the directory where the archive will be created.
         *            eg. c:/temp
         * @param zipPath
         *            The full path of the archive to create. eg.
         *            c:/temp/archive.zip
         * @throws IOException
         *             If anything goes wrong
         */
        public static void createZip(String directoryPath, String zipPath) throws IOException {
                FileOutputStream fOut = null;
                BufferedOutputStream bOut = null;
                ZipArchiveOutputStream tOut = null;

                try {
                        fOut = new FileOutputStream(new File(zipPath));
                        bOut = new BufferedOutputStream(fOut);
                        tOut = new ZipArchiveOutputStream(bOut);
                        addFileToZip(tOut, directoryPath, "");
                } finally {
                        tOut.finish();
                        tOut.close();
                        bOut.close();
                        fOut.close();
                }

        }

        /**
         * Creates a zip entry for the path specified with a name built from the
         * base passed in and the file/directory
         * name. If the path is a directory, a recursive call is made such that the
         * full directory is added to the zip.
         * 
         * @param zOut
         *            The zip file's output stream
         * @param path
         *            The filesystem path of the file/directory being added
         * @param base
         *            The base prefix to for the name of the zip file entry
         * 
         * @throws IOException
         *             If anything goes wrong
         */
        private static void addFileToZip(ZipArchiveOutputStream zOut, String path, String base) throws IOException {
                File f = new File(path);
                String entryName = base + f.getName();
                ZipArchiveEntry zipEntry = new ZipArchiveEntry(f, entryName);

                zOut.putArchiveEntry(zipEntry);

                if (f.isFile()) {
                        FileInputStream fInputStream = null;
                        try {
                                fInputStream = new FileInputStream(f); 
                                IOUtils.copy(fInputStream, zOut);
                                zOut.closeArchiveEntry();
                        } finally {
                                IOUtils.closeQuietly(fInputStream);
                        }

                } else {
                        zOut.closeArchiveEntry();
                        File[] children = f.listFiles();

                        if (children != null) {
                                for (File child : children) {
                                        addFileToZip(zOut, child.getAbsolutePath(), entryName + "/");
                                }
                        }
                }
        }
        

    /**
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws DataFormatException
     */
    public static String extractBytes(byte[] input) throws UnsupportedEncodingException, IOException, DataFormatException
    {
        Inflater ifl = new Inflater();   //mainly generate the extraction
        
        ifl.setInput(input);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
        byte[] buff = new byte[1024];
        while(!ifl.finished())
        {
            int count = ifl.inflate(buff);
            baos.write(buff, 0, count);
        }
        baos.close();
        byte[] output = baos.toByteArray();
 
        //System.out.println("Original: "+input.length);
        //System.out.println("Extracted: "+output.length);
        //System.out.println("Data:");
        //System.out.println(new String(output));
        return new String(output);
    }   
        
    
    /**
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws DataFormatException
     */
    public static String extractString(String input) throws UnsupportedEncodingException, IOException, DataFormatException
    {
        return extractBytes(input.getBytes());
    }
    
    /**
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String compressString(String data) throws UnsupportedEncodingException, IOException
    {
        return new String(compressBytes(data));
    }
    
        /**
         * @param data
         * @return
         * @throws UnsupportedEncodingException
         * @throws IOException
         */
        public static byte[] compressBytes(String data) throws UnsupportedEncodingException, IOException
    {
        byte[] input = data.getBytes("UTF-8");  //the format... data is the total string
        Deflater df = new Deflater();       //this function mainly generate the byte code
        df.setLevel(Deflater.BEST_COMPRESSION);
        df.setInput(input);
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);   //we write the generated byte code in this array
        df.finish();
        byte[] buff = new byte[1024];   //segment segment pop....segment set 1024
        while(!df.finished())
        {
            int count = df.deflate(buff);       //returns the generated code... index
            baos.write(buff, 0, count);     //write 4m 0 to count
        }
        baos.close();
        byte[] output = baos.toByteArray();
 
        //System.out.println("Original: "+input.length);
        //System.out.println("Compressed: "+output.length);
        return output;
    }

        /**
         * Extract zip file at the specified destination path.
         * NB:archive must consist of a single root folder containing everything
         * else
         * 
         * @param archivePath
         *            path to zip file
         * @param destinationPath
         *            path to extract zip file to. Created if it doesn't exist.
         */
        public static void extractZip(String archivePath, String destinationPath) {
                File archiveFile = new File(archivePath);
                File unzipDestFolder = null;

                try {
                        unzipDestFolder = new File(destinationPath);
                        String[] zipRootFolder = new String[] { null };
                        unzipFolder(archiveFile, archiveFile.length(), unzipDestFolder, zipRootFolder);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * Extract zip file at the specified destination path.
         * NB:archive must consist of a single root folder containing everything
         * else
         * 
         * @param archivePath
         *            path to zip file
         * @param zipRootFolder
         *            Root folder        
         * @param destinationPath
         *            path to extract zip file to. Created if it doesn't exist.
         */
        public static void extractZip(String archivePath, String[] zipRootFolder, String destinationPath) {
                File archiveFile = new File(archivePath);
                File unzipDestFolder = null;

                try {
                        unzipDestFolder = new File(destinationPath);
                        unzipFolder(archiveFile, archiveFile.length(), unzipDestFolder, zipRootFolder);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        
        /**
         * @param archivePath
         * @param zipRootFolder
         * @param destinationPath
         * @param subFolder
         */
        public static void extractZip(String archivePath, String[] zipRootFolder, String destinationPath,String subFolder) {
                File archiveFile = new File(archivePath);
                File unzipDestFolder = null;

                try {
                        unzipDestFolder = new File(destinationPath);
                        unzipSubFolder(archiveFile, archiveFile.length(), unzipDestFolder, zipRootFolder,subFolder);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        
        
        
        

        /**
         * Unzips a zip file into the given destination directory.
         * 
         * The archive file MUST have a unique "root" folder. This root folder is
         * skipped when unarchiving.
         * 
         * @return true if folder is unzipped correctly.
         */
        @SuppressWarnings("unchecked")
        private static boolean unzipFolder(File archiveFile, long compressedSize, File zipDestinationFolder,
                        String[] outputZipRootFolder) {

                ZipFile zipFile = null;
                try {
                        zipFile = new ZipFile(archiveFile);
                        byte[] buf = new byte[65536];

                        Enumeration entries = zipFile.getEntries();
                        while (entries.hasMoreElements()) {
                                ZipArchiveEntry zipEntry = (ZipArchiveEntry) entries.nextElement();
                                String name = zipEntry.getName();
                                name = name.replace('\\', '/');
                                int i = name.indexOf('/');
                                if (i > 0) {
                                        outputZipRootFolder[0] = name.substring(0, i);
                                }
                                name = name.substring(i + 1);

                                File destinationFile = new File(zipDestinationFolder, name);
                                if (name.endsWith("/")) {
                                        if (!destinationFile.isDirectory() && !destinationFile.mkdirs()) {
                                                logger.info("Error creating temp directory:" + destinationFile.getPath());
                                                return false;
                                        }
                                        continue;
                                } else if (name.indexOf('/') != -1) {
                                        // Create the the parent directory if it doesn't exist
                                        File parentFolder = destinationFile.getParentFile();
                                        if (!parentFolder.isDirectory()) {
                                                if (!parentFolder.mkdirs()) {
                                                		logger.info("Error creating temp directory:" + parentFolder.getPath());
                                                        return false;
                                                }
                                        }
                                }

                                FileOutputStream fos = null;
                                try {
                                        fos = new FileOutputStream(destinationFile);
                                        int n;
                                        InputStream entryContent = zipFile.getInputStream(zipEntry);
                                        while ((n = entryContent.read(buf)) != -1) {
                                                if (n > 0) {
                                                        fos.write(buf, 0, n);
                                                }
                                        }
                                } finally {
                                        if (fos != null) {
                                                fos.close();
                                        }
                                }
                        }
                        return true;

                } catch (IOException e) {
                		logger.info("Unzip failed:" + e.getMessage());
                } finally {
                        if (zipFile != null) {
                                try {
                                        zipFile.close();
                                } catch (IOException e) {
                                	logger.info("Error closing zip file");
                                }
                        }
                }

                return false;
        }
        
        
        
        /**
         * Unzips a zip file into the given destination directory.
         * 
         * The archive file MUST have a unique "root" folder. This root folder is
         * skipped when unarchiving.
         * 
         * @return true if folder is unzipped correctly.
         */
        @SuppressWarnings("unchecked")
        private static boolean unzipSubFolder(File archiveFile, long compressedSize, File zipDestinationFolder,
                        String[] outputZipRootFolder,String subFolder) {

                ZipFile zipFile = null;
                try {
                        zipFile = new ZipFile(archiveFile);
                        byte[] buf = new byte[65536];

                        Enumeration entries = zipFile.getEntries();
                        while (entries.hasMoreElements()) {
                                ZipArchiveEntry zipEntry = (ZipArchiveEntry) entries.nextElement();
                                String name = zipEntry.getName();
                                name = name.replace('\\', '/');
                                
                                /**
                                 * Skip anything other than subfolder
                                 */
                                if(!StringUtils.contains(name,subFolder)){
                                        continue;
                                }
                                int i = name.indexOf(subFolder);
                                if (i > 0) {
                                        outputZipRootFolder[0] = name.substring(0, i);
                                }
                                name = name.substring(i + subFolder.length());

                                if(StringUtils.isBlank(name)){
                                        name = "/";
                                }
                                
                                File destinationFile = new File(zipDestinationFolder, name);
                                if (name.endsWith("/")) {
                                        if (!destinationFile.isDirectory() && !destinationFile.mkdirs()) {
                                        		logger.info("Error creating temp directory:" + destinationFile.getPath());
                                                return false;
                                        }
                                        continue;
                                } else if (name.indexOf('/') != -1) {
                                        // Create the the parent directory if it doesn't exist
                                        File parentFolder = destinationFile.getParentFile();
                                        if (!parentFolder.isDirectory()) {
                                                if (!parentFolder.mkdirs()) {
                                                	logger.info("Error creating temp directory:" + parentFolder.getPath());
                                                        return false;
                                                }
                                        }
                                }

                                FileOutputStream fos = null;
                                try {
                                        fos = new FileOutputStream(destinationFile);
                                        int n;
                                        InputStream entryContent = zipFile.getInputStream(zipEntry);
                                        while ((n = entryContent.read(buf)) != -1) {
                                                if (n > 0) {
                                                        fos.write(buf, 0, n);
                                                }
                                        }
                                } finally {
                                        if (fos != null) {
                                                fos.close();
                                        }
                                }
                        }
                        return true;

                } catch (IOException e) {
                	logger.info("Unzip failed:" + e.getMessage());
                } finally {
                        if (zipFile != null) {
                                try {
                                        zipFile.close();
                                } catch (IOException e) {
                                	logger.info("Error closing zip file");
                                }
                        }
                }

                return false;
        }       
}
