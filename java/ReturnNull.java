public Role getOne(String roleId) {
    Role role = roleRepository.getOne(roleId);
    if (role == null) {
        return null;
    } else {
        return role;
    }
}