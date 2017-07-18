package com.kii.skeletonize_doclet;

import com.sun.javadoc.*;

public class Converter {

    public static Entity convert(ClassDoc classDoc) {
        if (classDoc.isInterface()) {
            return convertToInterface(classDoc);
        }
        if (classDoc.isEnum()) {
            return convertToEnum((classDoc));
        }
        return convertToClass(classDoc);
    }

    public static EnumSignature convertToEnum(ClassDoc classDoc) {
        EnumSignature enumSignature = new EnumSignature();
        enumSignature.modifiers = "public";
        enumSignature.name = processClassName(classDoc);

        enumSignature.packageName = classDoc.containingPackage().name();
        enumSignature.comment = classDoc.getRawCommentText();
        enumSignature.implementsDeclaration = implementsDeclaration(classDoc);
        for (ClassDoc inner : classDoc.innerClasses()) {
            enumSignature.innerClasses.add(convert(inner));
        }

        for (MethodDoc md : classDoc.methods()) {
            if ((md.name().equals("values") || md.name().equals("valueOf"))) {
                continue;
            }
            enumSignature.methods.add(convertMethod(md));
        }
        for (FieldDoc fd : classDoc.fields()) {
            if (isParcelableCreatorField(fd)) {
                continue;
            }
            enumSignature.fields.add(convertField(fd));
        }
        for (FieldDoc fd : classDoc.enumConstants()) {
            enumSignature.enumFields.add(convertEnumField(fd));
        }
        return enumSignature;
    }

    public static InterfaceSignature convertToInterface(ClassDoc classDoc) {
        InterfaceSignature interfaceSignature = new InterfaceSignature();
        String modifiers = classDoc.modifiers();
        modifiers = modifiers.replace("interface", "");
        interfaceSignature.modifiers = modifiers;

        interfaceSignature.name = processClassName(classDoc);
        interfaceSignature.generics = typeVariables(classDoc.typeParameters());
        interfaceSignature.packageName = classDoc.containingPackage().name();
        interfaceSignature.comment = classDoc.getRawCommentText();
        interfaceSignature.extendsDeclaration = extendsDeclaration(classDoc);

        for (MethodDoc md : classDoc.methods()) {
            MethodSignature ms = convertMethod(md);
            interfaceSignature.methods.add(convertMethod(md));
        }
        for (FieldDoc fd : classDoc.fields()) {
            if (isParcelableCreatorField(fd)) {
                continue;
            }
            interfaceSignature.fields.add(convertField(fd));
        }
        return interfaceSignature;
    }

    public static ClassSignature convertToClass(ClassDoc classDoc) {
        ClassSignature classSignature = new ClassSignature();
        classSignature.modifiers = classDoc.modifiers();
        classSignature.name = processClassName(classDoc);
        classSignature.classGenerics = typeVariables(classDoc.typeParameters());
        classSignature.packageName = classDoc.containingPackage().name();
        classSignature.comment = classDoc.getRawCommentText();
        classSignature.extendsDeclaration = extendsDeclaration(classDoc);
        classSignature.implementsDeclaration = implementsDeclaration(classDoc);
        for (ClassDoc inner : classDoc.innerClasses()) {
            classSignature.innerClasses.add(convert(inner));
        }

        for (MethodDoc md : classDoc.methods()) {
            classSignature.methods.add(convertMethod(md));
        }
        for (FieldDoc fd : classDoc.fields()) {
            if (isParcelableCreatorField(fd)) {
                continue;
            }
            classSignature.fields.add(convertField(fd));
        }
        return classSignature;
    }

    public static String processClassName(ClassDoc classDoc) {
        String name = classDoc.name();
        String[] spl = name.split("\\.");
        if (spl != null && spl.length > 0) {
            return spl[spl.length-1];
        }
        return name;
    }

    public static String extendsDeclaration(ClassDoc classDoc) {
        if (classDoc.isEnum() || classDoc.isInterface()) {
            return "";
        }
        Type t = classDoc.superclassType();
        String ret = "";
        if (t.toString().length() > 0 && !t.toString().equals("java.lang.Object")) {
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
                first = false;
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
            for (AnnotationDesc ad : p.annotations()) {
                args += "@" + ad.annotationType().name() + " ";
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
        String annotations = "";
        first = true;
        for (AnnotationDesc ad : methodDoc.annotations()) {
            if (!first) {
                annotations += " ";
            }
            annotations += "@" + ad.annotationType().name();
            first = false;
        }
        String declaration = modifier + " " + typeVars + " " + returnType + " " + methodName + " (" + args + ") ";
        String comment = methodDoc.commentText();
        return new MethodSignature(declaration, comment, methodBody(methodDoc), annotations);
    }

    public static String methodBody(MethodDoc methodDoc) {
        String returnType = methodDoc.returnType().typeName();
        if (returnType == "byte" || returnType == "char" || returnType == "short" || returnType == "int" || returnType == "long") {
            return "{ return 0; }";
        }
        if (returnType == "float") {
            return "{ return 0.0f; }";
        }
        if (returnType == "double") {
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

    public static boolean isParcelableCreatorField(FieldDoc fieldDoc) {
        String modifiers = fieldDoc.modifiers();
        String fieldName = fieldDoc.name();

        if (fieldName.equals("CREATOR") && modifiers.contains("static")) {
            return true;
        }
        return false;
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

    public static EnumFieldSignature convertEnumField(FieldDoc fieldDoc) {
        String fieldName = fieldDoc.name();

        String declaration = fieldName;
        String comment = fieldDoc.commentText();
        return new EnumFieldSignature(declaration, comment);
    }

    public static String indentComment(String indent, String comment) {
        String[] spl = comment.split("\\r?\\n");
        String ret = "";
        boolean first = true;
        for (String s : spl) {
            if (!first) {
                ret += "\n";
            }
            ret += indent + s;
            first = false;
        }
        return ret;
    }

}
