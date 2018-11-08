package binar.box.rest;

import binar.box.dto.*;
import binar.box.service.FileService;
import binar.box.service.LockService;
import binar.box.util.Constants;
import binar.box.util.Exceptions.FieldsException;
import binar.box.util.Exceptions.LockBridgesException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping(value = Constants.API)
public class LockController {

	@Autowired
	private LockService lockService;

	@Autowired
	private FileService fileService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock type", notes = "This endpoint is for admin, admin add lock types into database.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TYPE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<LockTypeDTOResponse> addLockType( @Valid @RequestBody LockTypeDTO lockTypeDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new FieldsException("Lock type fields are incorrect", "lock.type.invalid", bindingResult);
		}
		return new ResponseEntity<>(lockService.addLockType(lockTypeDTO), HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "ADMIN: Add lock images", notes = "This endpoint is for admin, admin add lock images.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT
			+ Constants.LOCK_TYPE_FILE_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	private ResponseEntity addLockTypeFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("id") long lockTypeId) throws IOException {
		fileService.saveFilesToLockType(files, lockTypeId);
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get lock type with lock type templates", notes = "User first step is to choose one lock type then one lock template type")
	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_TYPE_ENDPOINT)
	private ResponseEntity<List<LockTypeDTOResponse>> getLockTypes() {
		return new ResponseEntity<>(lockService.getLockTypes(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get lock sections", notes = "This endpoint response reveal available sections of panels.")
	@GetMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_SECTION_ENDPOINT)
	private ResponseEntity<List<LockSectionDTO>> lockSections() {
		return new ResponseEntity<>(lockService.getLockSections(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Add user lock", notes = "Mandatory fields: lockTypeTemplate,lockType." + "\n EX: {\r\n"
			+ "  \"fontColor\": \"BLUE\",\r\n" + "  \"fontSize\": 60,\r\n" + "  \"fontStyle\": \"ROBOTO\",\r\n"
			+ "  \"message\":\"MEsSAGE BECAUSE I CAN \",\r\n" + "  \"lockType\":3,\r\n" + "  \"lockTypeTemplate\":7\r\n"
			+ "} " + "\n This is the second user step to add a lock.")
	@PostMapping(value = Constants.LOCK_ENDPOINT)
	private ResponseEntity<LockResponseDTO> addLock(@RequestBody LockDTO lockDTO) {
			return new ResponseEntity<>(lockService.createUserLock(lockDTO), HttpStatus.CREATED);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@PutMapping(value = Constants.LOCK_ENDPOINT)
	@ApiOperation(value = "Update user lock", notes = "Mandatory fields: lockTypeTemplate,lockType." + "\n {\r\n"
			+ "  \"id\":1,\r\n" + "  \"fontColor\": \"string\",\r\n" + "  \"fontStyle\":\"ROBOTO\",\r\n"
			+ "  \"lockTypeTemplate\":6,\r\n" + "  \"lockType\": 3,\r\n" + "  \"longitude\": 0,\r\n"
			+ "  \"message\": \"string\",\r\n" + "  \"lockColor\":\"YELLOW\",\r\n" + "  \"lockSection\":1\r\n" + "\r\n"
			+ "}" + "\n This is the third user step to add a lock")
	private ResponseEntity<LockResponseDTO> updateLock(@RequestBody LockDTO lockDTO) {
		return new ResponseEntity<>(lockService.updateUserLock(lockDTO), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Get unpaid/in progress user locks", notes = "This is one optional step to complete \"add user lock\".")
	@GetMapping(value = Constants.LOCK_ENDPOINT)
	private ResponseEntity<List<LockResponseDTO>> getLocks() {
		return new ResponseEntity<>(lockService.getLocks(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Claim to remove user lock", notes = "Requesting this endpoint will send a token on user email which can be used to remove a lock.", hidden = true)
	@PostMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	private ResponseEntity claimToRemoveLock(@RequestParam("id") long id) {
		lockService.claimToRemoveUserLock(id);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "ex: eyJ0eXAiO....", dataType = "string", paramType = "header") })
	@ApiOperation(value = "Delete user lock", notes = "Requesting this endpoint will delete user lock using the token for more security.", hidden = true)
	@DeleteMapping(value = Constants.LOCK_ENDPOINT + Constants.LOCK_DELETE_USING_TOKEN)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void removeALockUsingToken(@RequestParam("token") String token) {
		lockService.removeUserLock(token);
	}

}
