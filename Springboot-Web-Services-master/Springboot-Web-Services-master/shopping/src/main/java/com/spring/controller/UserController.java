package com.spring.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.constants.ResponseCode;
import com.spring.constants.WebConstants;
import com.spring.model.Address;
import com.spring.model.Bufcart;
import com.spring.model.PlaceOrder;
import com.spring.model.Product;
import com.spring.model.User;
import com.spring.repository.AddressRepository;
import com.spring.repository.CartRepository;
import com.spring.repository.OrderRepository;
import com.spring.repository.ProductRepository;
import com.spring.repository.UserRepository;
import com.spring.response.cartResp;
import com.spring.response.prodResp;
import com.spring.response.response;
import com.spring.response.serverResp;
import com.spring.response.userResp;
import com.spring.util.Validator;
import com.spring.util.jwtUtil;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

	private static Logger logger = Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AddressRepository addrRepo;

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private OrderRepository ordRepo;

	@Autowired
	private jwtUtil jwtutil;

	@PostMapping("/signup") //FUNZIONA
	public ResponseEntity<serverResp> addUser(@Valid @RequestBody User user) {

		serverResp resp = new serverResp(); 
		try {
			if (Validator.isUserEmpty(user)) {
				resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
				resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
			} else if (!Validator.isValidEmail(user.getEmail())) {
				resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
				resp.setMessage(ResponseCode.INVALID_EMAIL_FAIL_MSG);
			} else {
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CUST_REG);
				User reg = userRepo.save(user);
				resp.setObject(reg);
			}
		} catch (Exception e) {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(e.getMessage());
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}

	@PostMapping("/verify")
	public ResponseEntity<serverResp> verifyUser(@Valid @RequestBody Map<String, String> credential) {

		String email = "";
		String password = "";
		if (credential.containsKey(WebConstants.USER_EMAIL)) {
			email = credential.get(WebConstants.USER_EMAIL);
		}
		if (credential.containsKey(WebConstants.USER_PASSWORD)) {
			password = credential.get(WebConstants.USER_PASSWORD);
		}
		User loggedUser = userRepo.findByEmailAndPasswordAndUsertype(email, password, WebConstants.USER_CUST_ROLE);
		serverResp resp = new serverResp();
		if (loggedUser != null) {
			String jwtToken = jwtutil.createToken(email, password, WebConstants.USER_CUST_ROLE);
			resp.setStatus(ResponseCode.SUCCESS_CODE);
			resp.setMessage(ResponseCode.SUCCESS_MESSAGE);
			resp.setAUTH_TOKEN(jwtToken);
			PlaceOrder po=new PlaceOrder();
			po.setUser(loggedUser);
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.OK);
	}

	@PostMapping("/addAddress")//FUNZIONA
	public ResponseEntity<userResp> addAddress(@Valid @RequestBody Address address,
			@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) {
		userResp resp = new userResp();
		if (Validator.isAddressEmpty(address)) {
			resp.setStatus(ResponseCode.BAD_REQUEST_CODE);
			resp.setMessage(ResponseCode.BAD_REQUEST_MESSAGE);
		} else if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				User user = jwtutil.checkToken(AUTH_TOKEN);
				user.setAddress(address);
				address.setUser(user);
				Address adr = addrRepo.saveAndFlush(address);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CUST_ADR_ADD);
				resp.setUser(user);
				resp.setAddress(adr);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<userResp>(resp, HttpStatus.ACCEPTED);
	}

	@PostMapping("/getAddress")//FUNZIONA
	public ResponseEntity<response> getAddress(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN) {

		response resp = new response();
		if (jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				User user = jwtutil.checkToken(AUTH_TOKEN);
				Address adr = addrRepo.findByUser(user);

				HashMap<String, String> map = new HashMap<>();
				map.put(WebConstants.ADR_NAME, adr.getAddress());
				map.put(WebConstants.ADR_CITY, adr.getCity());
				map.put(WebConstants.ADR_STATE, adr.getState());
				map.put(WebConstants.ADR_COUNTRY, adr.getCountry());
				map.put(WebConstants.ADR_ZP, String.valueOf(adr.getZipcode()));
				map.put(WebConstants.PHONE, adr.getPhonenumber());

				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CUST_ADR_ADD);
				resp.setMap(map);
				// resp.setAddress(adr);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<response>(resp, HttpStatus.ACCEPTED);
	}

	@PostMapping("/getProducts")//Funziona , ma non carica l'img
	public ResponseEntity<prodResp> getProducts(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {

		prodResp resp = new prodResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.LIST_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setOblist(prodRepo.findAll());
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<prodResp>(resp, HttpStatus.ACCEPTED);
	}

	@PostMapping("/addToCart")//FUNZIONA
	public ResponseEntity<serverResp> addToCart(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestBody Product productId) throws IOException {
System.out.println(productId+" ehy");
		serverResp resp = new serverResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				User loggedUser = jwtutil.checkToken(AUTH_TOKEN);
				Product cartItem = prodRepo.findByProductid(productId.getProductid());
System.out.println("a");
				Bufcart buf = new Bufcart();
				buf.setEmail(loggedUser.getEmail());
				buf.setQuantity(1);
				buf.setPrice(cartItem.getPrice());
				buf.setProductId(cartItem);
				buf.setProductname(cartItem.getProductname());
				Date date = new Date();
				buf.setDateAdded(date);
				System.out.println("a");
				cartRepo.save(buf);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.CART_UPD_MESSAGE_CODE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				System.out.println("ci simu?");
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}

	@GetMapping("/viewCart")//FUNZIONA
	public ResponseEntity<cartResp> viewCart(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {
		logger.info("Inside View cart request method");
		cartResp resp = new cartResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				logger.info("Inside View cart request method 2");
				User loggedUser = jwtutil.checkToken(AUTH_TOKEN);
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.VW_CART_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setOblist(cartRepo.findByEmail(loggedUser.getEmail()));
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<cartResp>(resp, HttpStatus.ACCEPTED);
	}

	@GetMapping("/updateCart")//FUNZIONA
	public ResponseEntity<cartResp> updateCart(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestParam String bufcartid,
			@RequestParam String quantity) throws IOException {

		cartResp resp = new cartResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				User loggedUser = jwtutil.checkToken(AUTH_TOKEN);
				Bufcart selCart = cartRepo.findByBufcartIdAndEmail(Integer.parseInt(bufcartid), loggedUser.getEmail());
				selCart.setQuantity(Integer.parseInt(quantity));
				cartRepo.save(selCart);
				List<Bufcart> bufcartlist = cartRepo.findByEmail(loggedUser.getEmail());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.UPD_CART_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setOblist(bufcartlist);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<cartResp>(resp, HttpStatus.ACCEPTED);
	}

	@GetMapping("/delCart")//FUNZIONA
	public ResponseEntity<cartResp> delCart(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN,
			@RequestParam String bufcartid) throws IOException {

		cartResp resp = new cartResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				User loggedUser = jwtutil.checkToken(AUTH_TOKEN);
				cartRepo.deleteByBufcartIdAndEmail(Integer.parseInt(bufcartid), loggedUser.getEmail());
				List<Bufcart> bufcartlist = cartRepo.findByEmail(loggedUser.getEmail());
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.DEL_CART_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
				resp.setOblist(bufcartlist);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<cartResp>(resp, HttpStatus.ACCEPTED);
	}

	@GetMapping("/placeOrder")//DA VERIFICARE
	public ResponseEntity<serverResp> placeOrder(@RequestHeader(name = WebConstants.USER_AUTH_TOKEN) String AUTH_TOKEN)
			throws IOException {

		serverResp resp = new serverResp();
		if (!Validator.isStringEmpty(AUTH_TOKEN) && jwtutil.checkToken(AUTH_TOKEN) != null) {
			try {
				User loggedUser = jwtutil.checkToken(AUTH_TOKEN);
				PlaceOrder po = ordRepo.findByOrderId(loggedUser.getOrder().getOrderId());
				po.setEmail(loggedUser.getEmail());
				Date date = new Date();
				po.setOrderDate(date);
				po.setOrderStatus(ResponseCode.ORD_STATUS_CODE);
				double total = 0;
				List<Bufcart> buflist = cartRepo.findAllByEmail(loggedUser.getEmail());
				for (Bufcart buf : buflist) {
					total =total+(buf.getQuantity() * buf.getPrice());
					Product p=prodRepo.findByProductid(buf.getProductId().getProductid());
					p.setQuantity(p.getQuantity()-buf.getQuantity());
					prodRepo.save(p);
				}
				po.setTotalCost(total);
				po.setUser(loggedUser);
				PlaceOrder res = ordRepo.save(po);
				buflist.forEach(bufcart -> {
					bufcart.setOrderId(po);
					cartRepo.save(bufcart);

				});
				resp.setStatus(ResponseCode.SUCCESS_CODE);
				resp.setMessage(ResponseCode.ORD_SUCCESS_MESSAGE);
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			} catch (Exception e) {
				resp.setStatus(ResponseCode.FAILURE_CODE);
				resp.setMessage(e.getMessage());
				resp.setAUTH_TOKEN(AUTH_TOKEN);
			}
		} else {
			resp.setStatus(ResponseCode.FAILURE_CODE);
			resp.setMessage(ResponseCode.FAILURE_MESSAGE);
		}
		return new ResponseEntity<serverResp>(resp, HttpStatus.ACCEPTED);
	}
}
