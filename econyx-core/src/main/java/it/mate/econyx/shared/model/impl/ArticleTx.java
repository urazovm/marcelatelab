package it.mate.econyx.shared.model.impl;

import it.mate.econyx.shared.model.Article;
import it.mate.econyx.shared.model.ArticleComment;
import it.mate.econyx.shared.model.HtmlContent;
import it.mate.econyx.shared.model.PortalUser;
import it.mate.gwtcommons.client.utils.ListPropertyWrapper;
import it.mate.gwtcommons.shared.model.CloneableProperty;
import it.mate.gwtcommons.shared.model.CloneablePropertyMissingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleTx implements Article {

  private static final long serialVersionUID = 7072306998735484215L;

  String id;
  
  String code;
  
  String name;
  
  Integer orderNm;
  
  String title;
  
  PortalUserTx author;
  
  Date created;
  
  List<ArticleCommentTx> comments = new ArrayList<ArticleCommentTx>();
  
  String content;
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getOrderNm() {
    return orderNm;
  }

  public void setOrderNm(Integer orderNm) {
    this.orderNm = orderNm;
  }

  public List<ArticleComment> getComments() {
    return new ListPropertyWrapper<ArticleComment, ArticleCommentTx>(comments, ArticleCommentTx.class);
  }

  @CloneableProperty (targetClass=ArticleCommentTx.class)
  public void setComments(List<ArticleComment> comments) {
    if (comments != null) {
      this.comments = new ArrayList<ArticleCommentTx>();
      for (ArticleComment comment : comments) {
        if (comment instanceof ArticleCommentTx) {
          this.comments.add((ArticleCommentTx)comment);
        } else {
          throw new CloneablePropertyMissingException(comment);
        }
      }
    } else {
      this.comments = null;
    }
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PortalUser getAuthor() {
    return author;
  }

  @CloneableProperty (targetClass=PortalUserTx.class)
  public void setAuthor(PortalUser author) {
    if (author == null) {
      this.author = null;
    } else if (author instanceof PortalUserTx) {
      this.author = (PortalUserTx)author;
    } else {
      throw new CloneablePropertyMissingException(author);
    }
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
  
  @Override
  public HtmlContent getHtml() {
    HtmlContentTx html = new HtmlContentTx();
    html.setType(HtmlContent.Type.MEDIUM);
    html.setContent(content);
    return html;
  }

  @CloneableProperty (targetClass=HtmlContentTx.class)
  public void setHtml(HtmlContent html) {
    if (html == null) {
      this.content = null;
    } else if (html instanceof HtmlContentTx) {
      this.content = html.getContent();
    } else {
      throw new CloneablePropertyMissingException(html);
    }
  }

}
