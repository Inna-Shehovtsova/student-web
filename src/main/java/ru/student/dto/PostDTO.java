package ru.student.dto;
import ru.student.entity.Post;
import ru.student.entity.Tag;

import java.util.stream.Collectors;

public class PostDTO {

        private Long postId;
        private String title;
        private String content;
        private String tags;

        public PostDTO() {
        }

        public PostDTO(Post post) {
            this.postId = post.getPostId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.tags = post.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(" "));
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }


}
