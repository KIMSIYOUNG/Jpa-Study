package com.springdata.han.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springdata.han.controller.MemberTeamDto;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);

    Page<MemberTeamDto> searchSimple(MemberSearchCondition condition, Pageable pageable);

    Page<MemberTeamDto> searchComplex(MemberSearchCondition condition, Pageable pageable);
}
