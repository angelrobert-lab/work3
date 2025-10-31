package com.converter;

import com.domain.dto.CommentSubmitDto;
import com.domain.po.CommentsPo;
import com.domain.vo.CommentsVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentsConverter {
    CommentsConverter INSTANCE = Mappers.getMapper(CommentsConverter.class);

    // PO与DTO互转
    CommentSubmitDto commentsPoToCommentsDto(CommentsPo po);
    CommentsPo commentsDtoToCommentsPo(CommentSubmitDto dto);
    /**
     * 从CommentSubmitDto转换为CommentsPo，并填充动态参数
     * @param dto 前端提交的评论DTO
     * @param userId 当前登录用户ID（外部传入）
     * @param username 当前登录用户昵称（外部传入）
     * @return 转换后的CommentsPo对象
     */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString())")
    CommentsPo submitDtoToCommentsPo(CommentSubmitDto dto, Long userId, String username);

    // PO与VO互转（VO可省略敏感字段，如uuid）
    CommentsVo commentsPoToCommentsVo(CommentsPo po);

    // 集合转换
    List<CommentSubmitDto> commentsPoListToCommentsDtoList(List<CommentsPo> poList);
    List<CommentsVo> commentsPoListToCommentsVoList(List<CommentsPo> poList);
}