package bran.packages.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCotroller {

	@GetMapping(value="/")
	public String index() {
		return "index";
	}
}
