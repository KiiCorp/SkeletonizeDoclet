package com.kii.skeletonize_doclet;

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                String imports = getImports(classDoc);
                JavaFile javaFile = new JavaFile(entity, imports);
                javaFile.render("", fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private static String getImports(ClassDoc classDoc) {
        File source = classDoc.position().file();
        String ret = "";
        try (FileReader fr = new FileReader(source);
             BufferedReader br = new BufferedReader(fr)
        ) {
            String line = null;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile("^import [a-zA-Z._*]+;");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    if (!first) {
                        ret += "\n";
                    }
                    ret += line;
                    first = false;
                }
            }
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

