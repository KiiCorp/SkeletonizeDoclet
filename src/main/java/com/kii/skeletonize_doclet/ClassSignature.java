package com.kii.skeletonize_doclet;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClassSignature implements Entity {

    public ClassSignature() {
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
    }

    /** High level classifier: class */
    public static final String CLASSIFIER = "class";
    /** package name of the class */
    public String packageName;
    /** class modifiers */
    public String modifiers;
    /** Non qualified class name */
    public String name;
    /** Generic types */
    public String classGenerics;
    /** Comment for the class */
    public String comment;
    /** Base  class */
    public String extendsDeclaration;
    /** Interfaces */
    public String implementsDeclaration;
    /** Methods */
    public List<MethodSignature> methods;
    /** Fields */
    public List<FieldSignature> fields;
    /** Inner classes */
    public List<Entity> innerClasses;

    @Override
    public String getClassifier() {
        return CLASSIFIER;
    }

    @Override
    public String getPackageName() {
        return this.packageName;
    }

    @Override
    public String getModifiers() {
        return this.modifiers;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getGenerics() {
        return this.classGenerics;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public String getImplementsDeclaration() {
        return this.implementsDeclaration;
    }

    @Override
    public String getExtendsDeclaration() {
        return this.extendsDeclaration;
    }

    @Override
    public List<MethodSignature> getMethods() {
        return this.methods;
    }

    @Override
    public List<FieldSignature> getFields() {
        return this.fields;
    }

    @Override
    public void render(String indent, OutputStream out) throws IOException {

        String path = this.getClass().getResource("/class.template").getFile();
        JtwigTemplate template = JtwigTemplate.fileTemplate(path);
        JtwigModel model = JtwigModel.newModel().with("class", this);

        try (
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            )
        {
            template.render(model, bas);
            String buffStr = bas.toString("utf-8");
            try (
                    StringReader sr = new StringReader(buffStr);
                    BufferedReader br = new BufferedReader(sr);
                    ) {
                String line = "";
                boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (!first) {
                        bw.newLine();
                    }
                    line = indent + line;
                    bw.write(line);
                    first = false;
                }

            }
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public String render(String indent) throws IOException {
        try (ByteArrayOutputStream bas = new ByteArrayOutputStream()) {
            render(indent, bas);
            String ret = bas.toString("utf-8");
            return ret;
        } catch (IOException e) {
            throw e;
        }
    }

}
