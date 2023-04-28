# Hephaestus

The most suitable text language for documentation. Native support for Markdown and HTML.

This language is dedicated to the inclusion of hierarchical relationships. The syntax is very similar to HTML or XML, yet it lies in a higher tier.

Hephaestus also supports extensions and secondary development to achieve high scalability. 

Official JavaScript Implementation: [Hephaestus-JS](https://github.com/ATATCTech/Hephaestus-JS)

## Installation

### Gradle

```groovy
dependencies {
    implementation 'com.atatctech:hephaestus:1.0.1'
}
```

### Maven

```xml
<dependency>
    <groupId>com.atatctech</groupId>
    <artifactId>hephaestus</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Usage

### Generate Hexpr

```java
Skeleton skeleton = new Skeleton("Welcome");
MDBlock mdBlock = new MDBlock(new Text("# Welcome to the world of Hephaestus"));
skeleton.setComponent(mdBlock);
System.out.println(skeleton.expr());
```

### Parse Hexpr

```java
Component component = Hephaestus.parse("<Welcome:(component={md:{# Welcome to the world of Hephaestus}};)>");
Skeleton skeleton = (Skeleton) component;
System.out.println(((MDBlock) skeleton.getComponent()).getMarkdown().getText());
```

## Hexpr

Hexpr (Hephaestus Expression) is the text expression for Hephaestus components.

### Using Brackets

Different brackets represent different meanings in Hexpr.

| Bracket | Usage                                                     |
| ------- | --------------------------------------------------------- |
| {}      | A component.                                              |
| []      | A collection of components (a special type of component). |
| ()      | An attribute set.                                         |
| <>      | A skeleton (a special type of component).                 |

### Component Specialization

The component is the largest unit in Hexpr. A component consists of the following parts:

```hepxr
{TAG_NAME:(ATTRIBUTES)CONTENT}
```

Specifically, TAG_NAME indicates the type of component. CONTENT can be everything depending on what the component needs.

Some components have the ability to contain other components, as known as `WrapperComponent`. In a `WrapperComponent`, you are believed to find a structure like `{TAG_NAME:(ATTRIBUTES)[CHILDREN]}`.

Except for some specific components such as `Text` or `MultiComponent`, most components are specified by tag names. For example, `{md:{# I love Hexpr}}` stands for a Markdown block.

To list all the supported tag names, use:

```java
String[] supportedTagNames = Hephaestus.listTagNames();
System.out.println(supportTagNames);
```

## Extensions

### Load Extensions

```java
// load the extension
Config.getInstance().scanPackage("{EXTENSION_PACKAGE}");
// check if successful
System.out.println(Hephaestus.listTagNames());
```

### Create an Extension

#### Register

To register a component, inherit from `Component` and annotate with `@ComponentConfig`.

Note that registering a component that has an existing tag name would override the current component.

```java
package com.example;

@ComponentConfig(tagName="myc")
public class MyComponent extends Component {
    public static Parser<MyComponent> PARSER;
    
    static {
        PARSER = expr -> new MyComponent();
    }
    
    public MyComponent() {}
    
    @Override
    public @NotNull String expr() {
        return generateExpr("This is my component.");
    }
}
```

`@ComponentConfig` requires an argument `tagName`, which specifies two things: the tag name appears in Hexpr and the file suffix when exporting to the file system.

The class must have a static field exactly named "PARSER".

A default constructor with no parameter must be included to support runtime reflection.

#### Load and Test

```java
Config.getInstance().scanPackage("com.example");

Component myComponent = new MyComponent();
String expr = myComponent.expr();
System.out.println(expr);
myComponent = Hephaestus.parseExpr(expr);
assert myComponent != null;
System.out.println(myComponent.getTagName());
```

```shell
{myc:This is my component.}
myc
```

### Replace an Existing Parser

Parsers are usually and should be defined as not final. Replacing a component's parser is extremely simple.

The following example replaces `Skeleton`'s parser.

```java
Skeleton.PARSER = WrapperComponent.makeParser(Skeleton.class);
```

