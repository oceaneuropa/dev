package org.orbit.component.runtime.tier1.account.service;

import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.model.tier1.account.UserAccountRTO;

public class UserAccountModelConverter {

	private static UserAccountModelConverter converter = new UserAccountModelConverter();

	public static UserAccountModelConverter getInstance() {
		return converter;
	}

	// ------------------------------------------------------------------------------------------
	// RTO -> DTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert UserAccountRTO to UserAccountDTO.
	 * 
	 * @param userAccount
	 * @return
	 */
	public UserAccountDTO toDTO(UserAccountRTO userAccount) {
		if (userAccount == null) {
			return null;
		}
		UserAccountDTO dto = new UserAccountDTO();

		dto.setUserId(userAccount.getUserId());
		dto.setPassword(userAccount.getPassword());
		dto.setEmail(userAccount.getEmail());
		dto.setFirstName(userAccount.getFirstName());
		dto.setLastName(userAccount.getLastName());
		dto.setPhone(userAccount.getPhone());
		dto.setCreationTime(userAccount.getCreationTime());
		dto.setLastUpdateTime(userAccount.getLastUpdateTime());
		dto.setActivated(userAccount.isActivated());

		return dto;
	}

	// ------------------------------------------------------------------------------------------
	// DTO -> RTO
	// ------------------------------------------------------------------------------------------
	/**
	 * Convert UserAccountDTO to UserAccountRTO.
	 * 
	 * @param userAccountDTO
	 * @return
	 */
	public UserAccountRTO toRTO(UserAccountDTO userAccountDTO) {
		if (userAccountDTO == null) {
			return null;
		}
		UserAccountRTO userAccount = new UserAccountRTO();

		userAccount.setUserId(userAccountDTO.getUserId());
		userAccount.setPassword(userAccountDTO.getPassword());
		userAccount.setEmail(userAccountDTO.getEmail());
		userAccount.setFirstName(userAccountDTO.getFirstName());
		userAccount.setLastName(userAccountDTO.getLastName());
		userAccount.setPhone(userAccountDTO.getPhone());
		userAccount.setCreationTime(userAccountDTO.getCreationTime());
		userAccount.setLastUpdateTime(userAccountDTO.getLastUpdateTime());
		userAccount.setActivated(userAccountDTO.isActivated());

		return userAccount;
	}

}

/// **
// * Convert UserRegistryException object to Error DTO.
// *
// * @param e
// * @return
// */
// public ErrorDTO toDTO(ServerException e) {
// if (e == null) {
// return null;
// }
//
// ErrorDTO dto = new ErrorDTO();
//
// dto.setCode(e.getCode());
// dto.setMessage(e.getMessage());
//
// if (e.getCause() != null) {
// String causeName = e.getCause().getClass().getName();
// String causeMessage = e.getCause().getMessage();
// dto.setDetail(causeName + " " + causeMessage);
//
// } else {
// String causeName = e.getClass().getName();
// dto.setDetail(causeName);
// }
// return dto;
// }
