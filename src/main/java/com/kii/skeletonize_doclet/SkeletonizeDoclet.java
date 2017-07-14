package com.kii.skeletonize_doclet;

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SkeletonizeDoclet extends Standard {

    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        String[][] options = root.options();
        String destDir = null;
        for (String[] option : options) {
            if (option[0].equals("-d")) {
                destDir = option[1];
                System.out.printf("-d: %s\n", option[1]);
            }
        }

        for (ClassDoc classDoc : classes) {
            if (isInnerClass(classDoc)) {
                continue;
            }
            classDoc.position().file();
            Entity entity = Converter.convert(classDoc);
            String fileName = entity.getName() + ".java";
            File file = new File(destDir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file))
            {
                JavaFile javaFile = new JavaFile(entity);
                javaFile.render("", fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private static boolean isInnerClass(ClassDoc classDoc) {
        String name = classDoc.name();
        if (name.contains(".")) {
            return true;
        }
        return false;
    }
}

