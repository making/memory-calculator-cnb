package am.ik.buildpacks;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassFileCounter {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("No argument passed!");
            System.exit(1);
        }
        final boolean verbose = "-v".equals(args[args.length - 1]);

        int total = 0;
        for (int i = 0; i < args.length; i++) {
            if (i == args.length - 1 && verbose) {
                break;
            }
            final String filepath = args[i];
            System.err.printf("---> Counting class files in %s.", filepath);
            final int count = count(new File(filepath), verbose);
            System.err.printf(":\t %d%n", count);
            total += count;
        }
        System.out.printf("%d%n", total);
    }


    static int count(File file, boolean verbose) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files == null) {
                return 0;
            }
            return Arrays.stream(files).mapToInt(f -> count(f, verbose)).sum();
        }
        final String fileName = file.getAbsolutePath();
        if (shouldCount(fileName)) {
            if (verbose) {
                System.err.printf("    Count %s%n", fileName);
            }
            return 1;
        }
        if (fileName.endsWith(".jar")) {
            try {
                final List<? extends ZipEntry> entries = Collections.list(new ZipFile(file).entries());
                return entries.stream().mapToInt(zipEntry -> count(zipEntry, verbose)).sum();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return 0;
    }

    static int count(ZipEntry zipEntry, boolean verbose) {
        if (zipEntry.isDirectory()) {
            return 0;
        }
        final String name = zipEntry.getName();
        if (shouldCount(name)) {
            if (verbose) {
                System.err.printf("    Count %s%n", name);
            }
            return 1;
        }
        return 0;
    }

    static boolean shouldCount(String fileName) {
        return fileName.endsWith(".class") || fileName.endsWith(".kt") || fileName.endsWith(".groovy");
    }
}
