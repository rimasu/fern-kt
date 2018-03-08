# Friendly Resource Notation (FERN)
A generic, generic structured, weakly typed value exchange stucture.

[![Build Status](https://travis-ci.org/rimasu/fern-kt.png?branch=master)](https://travis-ci.org/rimasu/node)

This projects has three main components
1. A generic weakly typed tree structure, designed to support easy
configuration of application components.
2. A standard decoder that can read a the very simple and
flexible FERN
3. A jackson decoder that can read convention JSON

License: MIT

## Generic Types

This library provides a small number of node types that can be composed to represent
rich configuration and control data.  It provides similar functionality to a
generic tree made from maps, list and objects, but with the following extra features:

1) Every node can construct a 'path' describing its location with respect to
the root of the structure. The path is composed from a mixture of field labels
and array indexes so that it is unambiguous even if a data structure is repetitive.

2) All leaf data is stored as strings. Data type coercion is done on request when
leaf data is retrieved by calling code. This preserves the raw leaf data from the source
right up to the point it is returned to the client. This allows for detailed
error data if the type coercion cannot be performed. It means that data type differences
expressed in storage formats like YAML and JSON are ignored (i.e. true is
equivalent to "true"). The also opens the potential for very simple storage formats later.

3) All access methods return using a result object. This is not ideal for high-volume
code, as it requires an object creation for each data access.  However for configuration
and control code it is ideal because when a expected value is missing its precise
location can be retrieved from results error information

The basic types are all implemented in the 'types' sub-model.

1) Node: Sealed class that defines the interface for nodes.

2) Null Node: A empty node.

3) Leaf Node: Stores small data types. Internally all types are stored as strings

4) List Node: Stores a ordered list of nodes. First node is at index one.

5) Struct Node: Stores a structured set of nodes, identified by labels.
