package com.kii.skeletonize_doclet;

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.ArrayList;
import java.util.List;

public class SkeletonizeDoclet extends Standard {

    public static List<ClassSignature> convert(RootDoc root) {
        List<ClassSignature> ret = new ArrayList<ClassSignature>();

        for (ClassDoc cls :root.classes()) {
            ClassSignature classSignature = new ClassSignature();
            classSignature.modifiers = cls.modifiers();
            classSignature.name = cls.name();
            classSignature.classGenerics = typeVariables(cls.typeParameters());
            classSignature.packageName = cls.containingPackage().name();
            classSignature.comment = cls.getRawCommentText();
            classSignature.extendsDeclaration = extendsDeclaration(cls);
            classSignature.implementsDeclaration = implementsDeclaration(cls);

            for (MethodDoc md : cls.methods()) {
                classSignature.methods.add(convertMethod(md));
            }
            for (FieldDoc fd : cls.fields()) {
                classSignature.fields.add(convertField(fd));
            }
            ret.add(classSignature);
        }
        return ret;
    }

    public static String extendsDeclaration(ClassDoc classDoc) {
        Type t = classDoc.superclassType();
        String ret = "";
        if (t.toString().length() > 0) {
            ret = " extends " + t.toString();
        }
        return  ret;
    }

    public static String implementsDeclaration(ClassDoc classDoc) {
        String ret = "";
        boolean first = true;
        for (Type t : classDoc.interfaceTypes()) {
            if (first) {
                ret += "implements ";
            } else {
                ret += ", ";
            }
            ret += t.toString();
        }
        return  ret;
    }

    public static String typeVariables(TypeVariable[] tvs) {
        String ret = "";
        boolean first = true;
        for (TypeVariable tv : tvs) {
            if (!first) {
                ret += ", ";
            }
            String typeName = tv.typeName();
            ret += typeName;
            if (tv.bounds().length == 1) {
                ret += " extends " + tv.bounds()[0].typeName();
            }
            first = false;
        }
        if (ret.length() > 0) {
            ret = "<" + ret + ">";
        }
        return  ret;
    }

    public static MethodSignature convertMethod(MethodDoc methodDoc) {
        String returnType = methodDoc.returnType().typeName();
        String methodName = methodDoc.name();
        String modifier = methodDoc.modifiers();
        String typeVars = typeVariables(methodDoc.typeParameters());
        String args = "";
        boolean first = true;
        for (Parameter p : methodDoc.parameters()) {
            if (!first) {
                args += ", ";
            }
            ParameterizedType pt = p.type().asParameterizedType();
            if (pt != null) {
                args += pt.toString();
            } else {
                args += p.type().typeName();
            }
            args += p.type().dimension();

            args += " " + p.name();
            first = false;
        }
        String declaration = modifier + " " + typeVars + " " + returnType + " " + methodName + " (" + args + ") " + methodBody(methodDoc);
        String comment = methodDoc.commentText();
        return new MethodSignature(declaration, comment);
    }

    public static String methodBody(MethodDoc methodDoc) {
        String returnType = methodDoc.returnType().typeName();
        if (returnType == "byte" || returnType == "char" || returnType == "short" || returnType == "int" || returnType == "long") {
            return "{ return 0; }";
        }
        if (returnType == "float" || returnType == "double") {
            return "{ return 0.0; }";
        }
        if (returnType == "boolean") {
            return "{ return false; }";
        }
        if (returnType == "void") {
            return "{}";
        }
        return "{ return null; }";
    }

    public static FieldSignature convertField(FieldDoc fieldDoc) {
        String modifiers = fieldDoc.modifiers();
        String fieldName = fieldDoc.name();
        String type = fieldDoc.type().typeName();
        String value = fieldDoc.constantValueExpression();

        String declaration = modifiers + " " + type + " " + fieldName;
        if (value != null && value.length() > 0) {
            declaration += " = " + value;
        }
        String comment = fieldDoc.commentText();
        return new FieldSignature(declaration, comment);
    }

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

        List<ClassSignature> csl = convert(root);
//        String path = ClassLoader.getSystemClassLoader().getResource("class.template").getFile();
        // FIXME: Resolve resource path.
        String path = "/Users/satoshi/git-wc/kiicloud/com.kii.skeletonize_doclet/build/resources/main/class.template";

        JtwigTemplate template = JtwigTemplate.fileTemplate(path);
        JtwigModel model = JtwigModel.newModel().with("class", csl.get(0));


        template.render(model, System.out);
        return true;
    }
}

