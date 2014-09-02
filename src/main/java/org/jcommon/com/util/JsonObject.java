package org.jcommon.com.util;

public abstract class JsonObject
{
  private String json_data;

  public JsonObject(String data)
  {
    this.json_data = data;
    try {
      Json2Object.json2Object(this, data);
    }
    catch (Exception e) {
      Json2Object.logger.error(data, e);
    }
  }

  public JsonObject()
  {
  }

  public String getJsonData() {
    return this.json_data;
  }

  public String toJsonStr() {
    try {
      return Json2Object.object2Json(this);
    }
    catch (Exception e) {
      Json2Object.logger.error("", e);
    }return this.json_data;
  }
}

