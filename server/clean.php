<?php

$files=array_diff(scandir('stack/'), array('..', '.'));
	foreach($files as $key => $value){
	unlink('stack/'.$value);
}

echo 'stack cleaned';

