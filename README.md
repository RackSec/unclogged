# unclogged

[![Build Status](https://travis-ci.org/RackSec/unclogged.svg?branch=master)](https://travis-ci.org/RackSec/unclogged)

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

## Usage

TODO

## License

Copyright © 2016 Rackspace Hosting, Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
