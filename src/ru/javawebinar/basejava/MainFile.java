package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("\n\n");
        printDirectory(new File("."), 0);
    }

    private static void printDirectory(File fsItem, int depth) {
        String tabChar = "\t".repeat(depth);
        if (fsItem.isFile()) {
            System.out.println(tabChar + "> " + fsItem.getName());
        } else {
            depth++;
            System.out.println(tabChar + fsItem.getName() + ": ");
            File[] list = fsItem.listFiles();
            if (list != null) {
                for (File file: list) {
                    if (file.getName().equals(".git") || file.getName().equals("out")) {
                        continue;
                    }
                    printDirectory(file, depth);
                }
            }
        }
    }
}
