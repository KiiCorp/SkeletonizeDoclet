package com.kii.skeletonize_doclet;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class JavaFile implements Renderer {
    public Entity entity;
    public String imports;

    public JavaFile(Entity entity, String imports) {
        this.entity = entity;
        this.imports = imports;
    }

    @Override
    public void render(String indent, OutputStream out) throws IOException {
        String path = this.getClass().getResource("/javafile.template").getFile();
        JtwigTemplate template = JtwigTemplate.fileTemplate(path);
        JtwigModel model = JtwigModel.newModel().with("class", this);
        template.render(model, out);
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
