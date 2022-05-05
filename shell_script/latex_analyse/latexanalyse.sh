#!/bin/sh

# @author: Konstantin Opora
# 06/11/2021
#
# analyses latex file:
# checks if file exits and is readable
#
# options to operate on latex file are:
#  	-g, --graphics      prints a list of all included graphics
#	-s, --structure     prints the structure of the input file
#	-u, --usedpackages  prints a list of the used packages and their options
#
# if performance is successful, return result and exit code = 0 
# else prints ERROR Message and help, also returns exit code > 0 



# Error List:
#	- "ERROR: exit code 1 -> no further parameters after -h|--help "
#			
#	- "ERROR: exit code 2 -> num of total parameters must be 2, if first is not -h|--help"
#			
#	- "ERROR: exit code 3 -> file does not exists or is not readable"
#			
#	- "ERROR: exit code 4 -> invalid OP"
#		

#*contains help text
usage() {
	cat << EOT
Usage:
latexanalyse.sh -h | latexanalyse.sh --help

  prints this help and exits

latexanalyse.sh INPUT OPTION

  INPUT is a valid latex-File (.tex)

  and OPTION is one of

  -g, --graphics      prints a list of all included graphics

  -s, --structure     prints the structure of the input file

  -u, --usedpackages  prints a list of the used packages and their options
EOT
}

# delets all comments (starts with %)
# @param file to delet comments from
function_deleteComments(){
	sed 's/%.*//gi;s/}/}\n/g' $1
}

# determents the exit and error message based on the given EXITCODE
# @param EXITCODE
function_getError(){
	case "$1" in
	1)
		echo "ERROR: exit code 1 -> no further parameters after -h|--help " 1>&2
		;;
	2)
		echo "ERROR: exit code 2 -> num of total parameters must be 2, if first is not -h|--help" 1>&2
		;;
	3)
		echo "ERROR: exit code 3 -> file does not exists or is not readable" 1>&2
		;;
	4)
		echo "ERROR: exit code 4 ->  invalid OP " 1>&2
		;;

	esac

	if [ "$1" -ne 0 ]
		then
			usage 1>&2
			exit $1
	fi
}

# prints a list of all included graphics
#@param: $1 - input file
#(
#	cat $1	 	-> prints filecontent of $1
#	sed ...		-> Remove all Comments(%) and Split CMDs with Newline Char
#	grep -o ... -> leaves only the CMDs mathcing the regex
#	sed ...  	-> leaves only the content inside the {} begind
#)
function_graphics(){
	function_deleteComments $1 \
	| grep -o '\\includegraphics[^}]*}' \
	| sed 's/.*{//gi;s/}//gi'
}

# prints the structure of the input file
#@param: $1 - input file
#(
#	cat $1					-> prints filecontent of $1
#	sed ...					-> Remove all Comments(%) and Split CMDs with Newline Char
#	grep -o ...				-> takes all CMDs that match one of the three regex
#	sed --expressions ...	-> format the content inside {} accordingly
#								-> --exprssion (allows multiple cmd to be executed)
#)
function_structure(){	
	function_deleteComments $1 \
	| grep -o '\\chapter[^}]*}\|\\section[^}]*}\|\\subsection[^}]*}' \
	| sed --expression 's/\\chapter.*{/ /
	s/\\section.*{/ \|-- /
	s/\\subsection.*{/     \|-- /
	s/}//'
}

# prints a list of the used packages and their options
#@param: $1 - input file
#(
#	cat $1	 	-> prints filecontent of $1
#	sed ...		-> Remove all Comments(%)
#	tr --delete -> deletes all /n 
#	sed ...		-> substitutes \ with /n, thus printing each latex cmd onto a new line
#	grep -o ...	-> greps latex CMDs matching the regex
#	sed ...		-> latex cmd : \usepackage[packageoptions] {packagename}
#					"catches" (userpackage\) ; (packageoptions);(packagename)
#					and reversing its order, printing 3:\2/
#				-> also performing on latex cmd of form :
#					\usepackage {packagename}
#
#					reversing its order, printing 2:\
#			
#				-> and delete comments/ latex cmd %.*
#	sort --dictionary-order
#			-> sorts finding in dictionary order
#)	
function_usedpackages(){
	function_deleteComments $1 \
	| tr --delete '\n' \
	| sed 's/\\/\n/gi' \
	| grep -o 'usepackage[^}]*}' \
	| sed -e 's/\(usepackage\)\[\(.*\)]{\(.*\)}/\3:\2/
	s/\(usepackage\){\(.*\)}/\2:/
	s/%.*//gi;s/ //gi' \
	| sort --dictionary-order
}

# carry for the exitcode
EXITCODE=0

# prints usage if -h or --help is invoked and returns exit code=0 
# further parameters after -h|--help -> ERROR exit code=1
if [ "X$1" = "X-h" ] || [ "X$1" = "X--help" ]
	then 
		if [ $# -eq 1 ]
			then 
				usage
				exit 0
		fi

		EXITCODE=1

# checks if num of parameters is valid, O/W exit code=2
elif [ $# -ne 2 ]
	then
		EXITCODE=2

#checks if file exists and is readable; O/W exit code=3
elif [ ! -r "$1" ]
	then
		EXITCODE=3

# Executes the function wanted by $1 if their was no Error (Exitcode still 0)
else
	case "$2" in
		"-g"|"--graphics")
			function_graphics $1
			;;
		"-s"|"--structure")
			function_structure $1
			;;
		"-u"|"--usedpackages")
			function_usedpackages $1
			;;
		*)
			EXITCODE=4
			;;
	esac
fi

function_getError $EXITCODE
