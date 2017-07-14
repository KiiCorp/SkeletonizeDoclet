package com.kii.skeletonize_doclet;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JavaFile implements Renderer {
    public Entity entity;

    public JavaFile(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void render(String indent, OutputStream out) throws IOException {
        String path = this.getClass().getResource("/javafile.template").getFile();
        JtwigTemplate template = JtwigTemplate.fileTemplate(path);
        JtwigModel model = JtwigModel.newModel().with("class", entity);
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
