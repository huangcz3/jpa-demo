package com.example.jpa.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Huangcz
 * @date 2018-11-12 12:17
 * @desc xxx
 */
@Repository
public interface TestMapper {

	List<Map> listUser();

}
