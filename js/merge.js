const merge = require('deepmerge');

//data
const x = { AA: { a: 1 } }
const y = { AA: { b: 2 } }
const z = { BB: { c: 3 } }

//method_1
const mg = merge.all([x,y,z]);
console.log(JSON.stringify(mg));
//execute : {"AA":{"a":1,"b":2},"BB":{"c":3}}

//method_2
Object.assign(x,y);
Object.assign(x,z);
console.log(JSON.stringify(x));
//execute : {"AA":{"b":2},"BB":{"c":3}}