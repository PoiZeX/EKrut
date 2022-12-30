package common;

public enum RolesEnum {
	user,  // from simulation
	registered, 
	member,
	customerServiceWorker,
	deliveryOperator,
	marketingWorker,
	marketingManager,
	supplyWorker,
	supplyManager, 
	regionManager,
	CEO;
	
	public static boolean isValidRole(String role) {
		for (RolesEnum r : RolesEnum.values()) {
			if (role.toLowerCase().equals(r.toString().toLowerCase()))
				return true;
		}
		return false;
	}
}

