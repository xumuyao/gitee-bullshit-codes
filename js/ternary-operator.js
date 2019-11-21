/**
 * js三元运算符之我是个什么东西？？？
 * @param {string} people 
 */
function isRole(people = "") {
  const user = "user";
  const admin = "admin";
  const vip = "vip";
  return people !== user
    ? people !== vip
      ? people !== admin
        ? "is dog"
        : "is admin"
      : "is vip"
    : "is user";
}
