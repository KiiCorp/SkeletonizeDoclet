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
            Entity entity = Converter.convert(classDoc);
            String fileName = entity.getName() + ".java";
            String packageDir = packageDir(classDoc);
            File dir = new File(destDir, packageDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
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

    private static String packageDir(ClassDoc classDoc) {
        String packageName = classDoc.containingPackage().name();
        String[] spl = packageName.split("\\.");
        String ret = "";
        boolean first = true;
        for (String s : spl) {
            if (!first) {
                ret += "/";
            }
            ret += s;
            first = false;
        }
        return ret;
    }

}

