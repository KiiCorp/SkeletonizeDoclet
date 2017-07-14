package com.kii.skeletonize_doclet;

import java.io.IOException;
import java.io.OutputStream;

public interface Renderer {
    void render(String indent, OutputStream out) throws IOException;
    String render(String indent) throws IOException;
}