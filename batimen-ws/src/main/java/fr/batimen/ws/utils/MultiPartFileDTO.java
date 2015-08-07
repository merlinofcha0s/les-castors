package fr.batimen.ws.utils;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * DTO qui permet a resteasy de matérialiser en objet le form que l'on envoi pour transferer des images
 *
 * @author Casaucau Cyril
 */
public class MultiPartFileDTO {

    @FormParam("content")
    @PartType("application/json")
    private String content;

    @FormParam("files")
    @PartType("application/octet-stream")
    private List<File> files;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiPartFileDTO that = (MultiPartFileDTO) o;
        return Objects.equals(content, that.content) &&
                Objects.equals(files, that.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, files);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MultiPartFileDTO{");
        sb.append("content=").append(content);
        sb.append(", files=").append(files);
        sb.append('}');
        return sb.toString();
    }
}
