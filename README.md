# unclogged

[![Build Status](https://travis-ci.org/RackSec/unclogged.svg?branch=master)](https://travis-ci.org/RackSec/unclogged)

[![Clojars Project](https://img.shields.io/clojars/v/unclogged.svg)](https://clojars.org/unclogged)

Easy, performant syslog for Clojure, exposed as a [manifold][manifold]
stream. Supports [RFC 3164][RFC3164] (classic BSD syslog), and the
[RFC5424][RFC5424] compliant [RFC 5426][RFC5426] (syslog over UDP) and
[RFC 6587][RFC6587] (syslog over TCP and TLS) specifications.

Since all of this is exposed using the stream abstractions from
[manifold][manifold], it works well with a variety of synchronous and
asynchronous programs, including [`core.async`][coreasync].

[manifold]: https://www.github.com/ztellman/manifold
[RFC3164]: http://tools.ietf.org/html/rfc3164
[RFC5424]: http://tools.ietf.org/html/rfc5424
[RFC5426]: http://tools.ietf.org/html/rfc5426
[RFC6587]: http://tools.ietf.org/html/rfc5426
[coreasync]: https://github.com/clojure/core.async

## Motivation

A couple of reasons you might want this:

- By having the API be a stream, your code becomes much easier to test. No
  need to set up a real syslog server; just see what comes out the other end.
- You can also use Clojure-like shorthand for severities, facilities and the
  like, for example, you can use `:info` (short for `:informational`, the name
  used in the spec, which also works) or even `"INFO"` instead of having to
  remember that that's severity 6.
- You can log strings or structured messages; they'll be serialized
  appropriately regardless.
- Breadth of different specs and transports (UDP, TCP, TLS) supported.

## Usage

TODO

## License

Copyright © 2016 Rackspace Hosting, Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
