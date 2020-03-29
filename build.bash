# this script is called when the judge is building your compiler.
# no argument will be passed in.
set -e
cd "$(dirname "$0")"
mkdir -p bin
find ./src -name *.java | javac -d bin -classpath "/ulib/java/antlr-4.8-complete.jar:/mnt/c/Users/Dedicatus545/Desktop/Mx_star_Compiler/lib/antlr-4.8-complete.jar" @/dev/stdin
