class Person {
  get object() {
    return this.myObject;
  }
  set object(val) {
    this.myObject = val;
  }
  borrowMoney(amount) {
    console.log(`\r\n借${amount}块钱`);
    console.log(`得先问问我对象`);
    const suggestion = this.askMyObject();
    if (suggestion) {
      console.log('...')
      console.log('我对象说，你说啥？');
    } else {
      console.log('你不是没有对象吗');
      console.log('是呀，那就没得问了');
    }
  }
  askMyObject() {
    if (Object.prototype.toString.call(this.object) === '[object Object]') {
      return true;
    } else {
      return false;
    }
  }
}
const me = new Person();
me.borrowMoney(1000)

const you = new Person();
you.object = {
  weight: '500斤的瘦子'
};
you.borrowMoney(1000)