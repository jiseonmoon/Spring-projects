package kr.co.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.domain.Node;
import kr.co.service.MenuService;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/menu/*")
@AllArgsConstructor
public class MenuController {

	private MenuService service;

	@GetMapping("/main")
	public void main(Model model) {
		Map<String, Object> map = service.tree();
		Map<Integer, Node> nodeMap = (Map<Integer, Node>) map.get("nodeMap");
		Map<Integer, Integer> noMap = (Map<Integer, Integer>) map.get("noMap");
		Map<Integer, List<Integer>> levelMap = (Map<Integer, List<Integer>>) map.get("levelMap");
		model.addAttribute("nodeMap", nodeMap);
		model.addAttribute("noMap", noMap);
		model.addAttribute("levelMap", levelMap);
	}

}
