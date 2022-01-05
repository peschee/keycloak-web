package org.keycloak.webbuilder;

import org.keycloak.webbuilder.misc.ChangeLogEntry;
import org.keycloak.webbuilder.utils.JsonParser;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Versions extends LinkedList<Versions.Version> {

    public Versions(File versionsDir, JsonParser json) {
        for (File versionFile : versionsDir.listFiles((dir, name) -> name.endsWith(".json"))) {
            add(json.read(versionFile, Version.class));
        }
        Collections.sort(this);

        get(0).setLatest(true);
    }

    public Version getLatest() {
        return get(0);
    }

    public Version getPrevious() {
        return getMajorMinor().get(1);
    }

    public List<Version> getMajorMinor() {
        Map<String, Version> map = new HashMap<>();
        for (Version v : this) {
            if (!map.containsKey(v.getVersionShorter())) {
                map.put(v.getVersionShorter(), v);
            }
        }
        LinkedList<Version> l = new LinkedList<>(map.values());
        Collections.sort(l);
        return l;
    }

    public static class Version implements  Comparable<Version> {

        private Date date;

        private String version;

        private String documentationTemplate;

        private String downloadTemplate;

        private String blogTemplate;

        private String wildflyVersionAdapter;

        private String wildflyVersionAdapterDeprecated;

        private boolean latest;

        private String releaseNotes;

        private String migrationNotes;

        private ChangeLog changes;

        public boolean isFinal() {
            return true;
        }

        public String getVersionShort() {
            String[] split = version.split("\\.");
            return split[0] + "." + split[1] + "." + split[2];
        }

        public String getVersionShorter() {
            String[] split = version.split("\\.");
            return split[0] + "." + split[1];
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBlogTemplate() {
            return blogTemplate;
        }

        public void setBlogTemplate(String blogTemplate) {
            this.blogTemplate = blogTemplate;
        }

        public String getDocumentationTemplate() {
            return documentationTemplate;
        }

        public void setDocumentationTemplate(String documentationTemplate) {
            this.documentationTemplate = documentationTemplate;
        }

        public String getDownloadTemplate() {
            return downloadTemplate;
        }

        public void setDownloadTemplate(String downloadTemplate) {
            this.downloadTemplate = downloadTemplate;
        }

        public String getWildflyVersionAdapter() {
            return wildflyVersionAdapter;
        }

        public void setWildflyVersionAdapter(String wildflyVersionAdapter) {
            this.wildflyVersionAdapter = wildflyVersionAdapter;
        }

        public String getWildflyVersionAdapterDeprecated() {
            return wildflyVersionAdapterDeprecated;
        }

        public void setWildflyVersionAdapterDeprecated(String wildflyVersionAdapterDeprecated) {
            this.wildflyVersionAdapterDeprecated = wildflyVersionAdapterDeprecated;
        }

        public String getDocumentationVersion() {
            return latest ? "" : "v/" + version + "/";
        }

        public void setLatest(boolean latest) {
            this.latest = latest;
        }

        public String getReleaseNotes() {
            return releaseNotes;
        }

        public void setReleaseNotes(String releaseNotes) {
            this.releaseNotes = releaseNotes;
        }

        public String getMigrationNotes() {
            return migrationNotes;
        }

        public void setMigrationNotes(String migrationNotes) {
            this.migrationNotes = migrationNotes;
        }

        public ChangeLog getChanges() {
            return changes;
        }

        public void setChanges(ChangeLog changes) {
            this.changes = changes;
        }

        @Override
        public int compareTo(Version o) {
            String[] v1 = version.split("\\.");
            String[] v2 = o.getVersion().split("\\.");

            int r = stringNumberCompareTo(v2[0], v1[0]);
            if (r != 0) {
                return r;
            }

            r = stringNumberCompareTo(v2[1], v1[1]);
            if (r != 0) {
                return r;
            }

            r = stringNumberCompareTo(v2[2], v1[2]);
            return r;
        }

        private int stringNumberCompareTo(String a, String b) {
            return Integer.valueOf(a).compareTo(Integer.valueOf(b));
        }
    }

    public static class ChangeLog {

        private List<ChangeLogEntry> entries;

        public ChangeLog(List<ChangeLogEntry> entries) {
            this.entries = entries;
        }

        public List<ChangeLogEntry> getAll() {
            return entries;
        }

        public List<ChangeLogEntry> getBugs() {
            return entries.stream().filter(e -> e.getKind().equals("bug")).collect(Collectors.toList());
        }

        public List<ChangeLogEntry> getFeatures() {
            return entries.stream().filter(e -> e.getKind().equals("feature")).collect(Collectors.toList());
        }

        public List<ChangeLogEntry> getEnhancements() {
            return entries.stream().filter(e -> e.getKind().equals("enhancement")).collect(Collectors.toList());
        }

        public List<ChangeLogEntry> getDeprecations() {
            return entries.stream().filter(e -> e.getKind().equals("deprecation")).collect(Collectors.toList());
        }
    }

}
