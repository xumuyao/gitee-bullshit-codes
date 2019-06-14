<?php
/**
 * Created by PhpStorm.
 * User: Beacon
 * Date: 2019/6/14
 * Time: 15:47
 */

$arr1=['a'=>1,'b'=>2,'c'=>3];

$arr2=['a'=>100,'b'=>200,'c'=>300];

foreach ($arr1 as &$v){
    echo $v."    ";
    $v=$v+1;
    echo $v."    ";
}


foreach ($arr2 as $v){
    //随便干嘛
}

echo "arr1 is :<br>";
var_dump($arr1);


echo "<br><br>";

echo "arr2 is :<br>";
var_dump($arr2);

/*
 arr1 is :
array (size=3)
  'a' => int 2
  'b' => int 3
  'c' => int 300


arr2 is :
array (size=3)
  'a' => int 100
  'b' => int 200
  'c' => int 300
*/

