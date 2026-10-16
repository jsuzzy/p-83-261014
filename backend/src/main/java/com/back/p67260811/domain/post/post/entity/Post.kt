package com.back.p67260811.domain.post.post.entity

import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.domain.post.comment.entity.PostComment
import com.back.p67260811.global.exception.ServiceException
import com.back.p67260811.global.jpa.entity.BaseEntity
import jakarta.persistence.*

@Entity
class Post(
    @field:ManyToOne(fetch = FetchType.LAZY)
    var author: Member,

    var title: String,

    var content: String,
) : BaseEntity() {

    @field:OneToMany(
        mappedBy = "post",
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var comments: MutableList<PostComment> = mutableListOf()

    fun findCommentById(id: Int): PostComment =
        comments.find { it.id == id }
            ?: throw IllegalArgumentException("해당 댓글이 존재하지 않습니다")

    fun addComment(content: String): PostComment =
        PostComment(content, this).also {
            comments.add(it)
        }

    fun removeComment(id: Int) {
        comments.removeAll { it.id == id }
    }

    fun modifyComment(id: Int, content: String?): PostComment =
        findCommentById(id).apply {
            modify(content)
        }

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }

    fun checkActorModify(actor: Member) {
        if (author.id != actor.id) {
            throw ServiceException("403-1", "수정 권한이 없습니다.")
        }
    }

    fun checkActorDelete(actor: Member) {
        if (author.id != actor.id) {
            throw ServiceException("403-2", "삭제 권한이 없습니다.")
        }
    }

//    fun getComments(): List<PostComment> = comments.toList()
//    fun getTitle(): String = title
//    fun getContent(): String = content
}