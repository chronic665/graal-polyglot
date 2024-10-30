package com.test;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.SandboxPolicy;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class PolyglotTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private Context createContext() {
        final var engine = Engine.newBuilder("js")
                .out(out)
                .err(err)
                .sandbox(SandboxPolicy.CONSTRAINED)
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        final var hostAccess = HostAccess.newBuilder()
                .allowAccessAnnotatedBy(HostAccess.Export.class)
                .allowImplementationsAnnotatedBy(HostAccess.Implementable.class)
                .allowArrayAccess(true)
                .allowListAccess(true)
                .allowMapAccess(true)
                .methodScoping(true)
                .allowMutableTargetMappings()
                .build();
        return Context.newBuilder("js")
                .engine(engine)
                .allowHostAccess(hostAccess)
                .sandbox(SandboxPolicy.CONSTRAINED)
                .build();
    }

    @Test
    void test_fails() {
        var context = createContext();
        var myMap = new HashMap<String, Object>();
        var inner = new HashMap<String, Object>();
        inner.put("k1", "v1");
        myMap.put("foo", inner);
        var script = """
        (function (myMap) {
            let foo = myMap.get("foo").set("k2", "v2");
            foo.set('bar', {"k3": "v3"});
            return myMap
        })
        """;
        var function = context.eval("js", script);
        var result = function.execute(myMap);
        System.out.println(result);
    }

    @Test
    void test2_this_works() {
        var context = createContext();
        var myMap = new HashMap<String, Object>();
        var inner = new HashMap<String, Object>();
        inner.put("k1", "v1");
        myMap.put("foo", inner);
        var script = """
        (function (myMap) {
            let output = {}
            output.foo = {"k": "v"};
            output.bar = new Map([["k3","v3"]]);
            return output
        })
        """;
        var function = context.eval("js", script);
        var result = function.execute(myMap);
        System.out.println(result);
    }

}
