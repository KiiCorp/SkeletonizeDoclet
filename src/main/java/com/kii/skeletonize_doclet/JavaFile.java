package com.kii.skeletonize_doclet;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JavaFile implements Renderer {
    public Entity entity;
    // FIXME: Resolve resource path.
    static final String TEMPLATE_PATH = "/Users/satoshi/git-wc/kiicloud/com.kii.skeletonize_doclet/build/resources/main/javafile.template";

    public JavaFile(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void render(String indent, OutputStream out) throws IOException {
        JtwigTemplate template = JtwigTemplate.fileTemplate(TEMPLATE_PATH);
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
