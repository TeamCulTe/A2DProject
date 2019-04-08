package com.imie.a2dev.teamculte.readeo.Utils;

import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * File util class used to perform actions on files.
 */
public abstract class FileUtil {
    /**
     * From a source file, copy it into a destination file given in parameter.
     * @param source The file to copy.
     * @param destination The output file.
     * @throws IOException Exception if file not found.
     */
    public static void copy(File source, File destination) throws IOException {
        FileInputStream fIs = new FileInputStream(source);

        try {
            FileOutputStream fOS = new FileOutputStream(destination);
            try {
                int len;
                byte[] buf = new byte[1024];

                while ((len = fIs.read(buf)) > 0) {
                    fOS.write(buf, 0, len);
                }
            } finally {
                fOS.close();
            }
        } finally {
            fIs.close();
        }
    }

    /**
     * From a zipped source file, unzip and copy it to a destination folder.
     * @param source The zipped file.
     * @param destination The destination folder.
     * @param fileName The name of the destination file.
     * @throws IOException Exception if file not found.
     */
    public static void unzip(File source, File destination,@Nullable String fileName) throws IOException {
        ZipInputStream zIS = new ZipInputStream(new BufferedInputStream(new FileInputStream(source)));
        
        try {
            ZipEntry zE;
            int count;

            byte[] buffer = new byte[1024];

            while ((zE = zIS.getNextEntry()) != null) {
                File file = new File(destination, (fileName != null) ? fileName : zE.getName());
                File dir = zE.isDirectory() ? file : file.getParentFile();

                if (!dir.isDirectory() && !dir.mkdirs()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                
                if (zE.isDirectory()) {
                    continue;
                }
                
                FileOutputStream fOS = new FileOutputStream(file);

                try {
                    while ((count = zIS.read(buffer)) != -1) {
                        fOS.write(buffer, 0, count);
                    }
                } finally {
                    fOS.close();
                }
            }
        } finally {
            zIS.close();
        }
    }


}
