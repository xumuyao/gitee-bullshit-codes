let value1 = 'outer value'

(function() {
  console.log(value1) // ReferenceError
  let value1 = 'inner value' // defined value1
}())