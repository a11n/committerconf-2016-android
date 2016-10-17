package de.ad.myapplication.bl;

public class Session {
  private final String id;
  private final String title;
  private final String speaker;
  private final String tags;
  private final String description;

  public Session(String id, String title, String speaker, String tags, String description) {
    this.id = id;
    this.title = title;
    this.speaker = speaker;
    this.tags = tags;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getSpeaker() {
    return speaker;
  }

  public String getTags() {
    return tags;
  }

  public String getDescription() {
    return description;
  }
}
