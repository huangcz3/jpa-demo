package com.example.jpa.controller;

import com.example.jpa.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


/**
 * @author Huangcz
 * @date 2018-11-01 14:02
 * @desc xxx
 */
@RestController
public class TestController {

//    @Autowired
//    private UserRepository userRepository;
//
//
//    @PostMapping("/add")
//    public String addUser(@RequestBody User user){
//        userRepository.save(user);
//        return "success";
//    }
//
//    @GetMapping("/findAll")
//    public Iterable<User> getAllUser(){
//
//        return userRepository.findAll();
//    }


	@Autowired
	private TestMapper testMapper;


	@GetMapping("/list")
	public List<Map> list() {
		return testMapper.listUser();
	}

	@GetMapping("/testRest")
	public String testRest(){
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject("http://www.baidu.com",String.class);
	}



}
