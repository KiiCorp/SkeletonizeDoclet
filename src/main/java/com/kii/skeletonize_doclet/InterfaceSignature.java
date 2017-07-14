package com.kii.skeletonize_doclet;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InterfaceSignature implements Entity {

    public static final String CLASSIFIER = "interface";
    public String packageName;
    public String modifiers;
    public String name;
    public String generics;
    public String comment;
    public String extendsDeclaration;
    public List<MethodSignature> methods;
    public List<FieldSignature> fields;

    public InterfaceSignature() {
        methods = new ArrayList<>();
        fields = new ArrayList<>();
    }

    @Override
    public String getClassifier() {
        return CLASSIFIER;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getModifiers() {
        return modifiers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGenerics() {
        return generics;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getImplementsDeclaration() {
        return "";
    }

    @Override
    public String getExtendsDeclaration() {
        return extendsDeclaration;
    }

    @Override
    public List<MethodSignature> getMethods() {
        return methods;
    }

    @Override
    public List<FieldSignature> getFields() {
        return fields;
    }

    @Override
    public void render(String indent, OutputStream out) throws IOException {

        String path = this.getClass().getResource("/interface.template").getFile();
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
