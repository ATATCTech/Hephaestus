# Hephaestus

The most suitable text language for documentation. Native support for Markdown and HTML.

The language is designed to be as compact as possible to reduce the overhead of network transmission, and at the same time, it supports generating outlines.

## Installation

### Gradle

```groovy
dependencies {
    implementation 'com.atatctech:hephaestus:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.atatctech</groupId>
    <artifactId>hephaestus</artifactId>
    <version>1.0.0</version>
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
Component component = Hephaestus.parseExpr("<Welcome:(component={md:{# Welcome to the world of Hephaestus}};)>");
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
{TAG_NAME:CONTENT}
```

Specifically, TAG_NAME indicates the type of component. CONTENT can be everything depending on what the component needs.

Some components have the ability to contain other components, as known as `WrapperComponent`. In a `WrapperComponent`, you are believed to find a structure like `{TAG_NAME:(ATTRIBUTES)[CHILDREN]}`.

Except for some specific components such as `Text` or `MultiComponent`, most components are specified by tag names. For example, `{md:{# I love Hexpr}}` stands for a Markdown block.

To list all the supported tag names, use:

```java
String[] supportedTagNames = Hephaestus.listTagNames();
System.out.println(supportTagNames);
```

