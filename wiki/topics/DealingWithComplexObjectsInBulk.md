# Dealing with complex objects in bulk

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)

Some DTO in complex systems can grow huge and you may start seeing performance issued with GeDA.

However, at this point we stand back to take a look at the bigger picture and try to remember what DTO's try to achieve:

* Provide only the necessary information
* Be lightweight to reduce traffic

So lets go through an example problem step by step and re-think how we do things.

## Problem

We have a blog system. Within it as a moderator we want to list all users and for each one we want to view all their post and then if we wish to we want to see replies to those posts.

So pretty simple domain model may look like this:

![geda-complex-problem-domain.png](https://github.com/inspire-software/geda-genericdto/blob/master/wiki/topics/geda-complex-problem-domain.png?raw=true)

## Domain model


```java

package com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * .
 * <p/>
 * User: denispavlov
 * Date: Jul 1, 2012
 * Time: 12:26:52 PM
 */
public class UserImpl implements User {

    private static Long dbCounter = 0L;

    private Long userId;
    private String username;
    private Collection<UserEntry> entries = new ArrayList<UserEntry>();

    public UserImpl() {
        this.userId = ++dbCounter;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Collection<UserEntry> getEntries() {
        return entries;
    }

    public void setEntries(final Collection<UserEntry> entries) {
        this.entries = entries;
    }

    public UserEntry createEntry() {
        final UserEntry entry = new UserEntryImpl(this);
        entries.add(entry);
        return entry;
    }
}
```


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;

import java.util.ArrayList;
import java.util.Collection;

public class UserEntryImpl implements UserEntry {

    private static Long dbCounter = 0L;

    private Long entryId;
    private User user;
    private String title;
    private String body;
    private Collection<UserEntryReply> replies = new ArrayList<UserEntryReply>();

    public UserEntryImpl() {
        this.entryId = ++dbCounter;
    }

    public UserEntryImpl(final User user) {
        this();
        this.user = user;
    }

    public Long getEntryId() {
        return entryId;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Collection<UserEntryReply> getReplies() {
        return replies;
    }

    public void setReplies(final Collection<UserEntryReply> replies) {
        this.replies = replies;
    }

    public UserEntryReply createReply(final User replier) {
        final UserEntryReply reply = new UserEntryReplyImpl(this, replier);
        replies.add(reply);
        return reply;
    }
}
```


```java

package com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;

public class UserEntryReplyImpl implements UserEntryReply {

    private UserEntry originalEntry;
    private UserEntry replyEntry;

    public UserEntryReplyImpl() {
    }

    public UserEntryReplyImpl(final UserEntry originalEntry, final User replier) {
        this();
        this.originalEntry = originalEntry;
        this.replyEntry = replier.createEntry();
        this.replyEntry.setTitle("RE: " + this.originalEntry.getTitle());
    }

    public UserEntry getOriginalEntry() {
        return originalEntry;
    }

    public UserEntry getReplyEntry() {
        return replyEntry;
    }

}
```

## Some observations and decisions

Immediately we can see that depending on how we map the DTO object to our domain model we may end up loading quite a bit of information.

In fact it is even possible here to create mapping in such a way that we get infinite paths, like user.getEntries().next().getUser() ....

So what would be the best way to deal with this?

Lets outline our goals and what information we need to achieve those

Goals | What we need
We want to see list of users |list of user objects with username + id
We want to see their posts |a user object and list of posts with body + title + id
We want to be able to see replies to those post |an entry object and list of replies with body + title + id + userId

## DTO model

Therefore we would need:

* BaseUserDTO object that will contain basic info without user blog entries for list
* UserDTO<extends BaseUserDTO> that has blog entries as BaseUserEntryDTO collection for viewing account
* UserEntryDTO<extends BaseUserEntryDTO> that has replies collection which are BaseUserEntryDTO and also we need BaseUserDTO here, just in case we need to look up the user - this is for viewing blog entry
* With replies we pretty much can get away with reusing the UserEntryDTO and BaseUserEntryDTO  

So here it is:


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;

/**
 * Lightweight object to be used in lists.
 */
@Dto
public class BaseUserDTOImpl implements BaseUserDTO {

    @DtoField(readOnly = true)
    private Long userId;
    @DtoField
    private String username;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}
```


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;

import java.util.List;

/**
 * Full user object + entries - prossible use is CRUD.
 * Note that entries are BaseUserEntryDTO to prevent loading of
 * user and replies info until we really need it.
 */
@Dto
public class UserDTOImpl extends BaseUserDTOImpl implements UserDTO {

    @DtoCollection(dtoBeanKey = "BaseUserEntryDTO",
                   dtoToEntityMatcherKey = "BaseUserEntryDTOMatcher",
                   entityGenericType = UserEntry.class)
    private List<BaseUserEntryDTO> entries;

    public List<BaseUserEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(final List<BaseUserEntryDTO> entries) {
        this.entries = entries;
    }
}
```


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;

/**
 * Lightweight object to generate list of entries.
 */
@Dto
public class BaseUserEntryDTOImpl implements BaseUserEntryDTO {

    @DtoField(readOnly = true)
    private Long entryId;
    @DtoField
    private String title;
    @DtoField
    private String body;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(final Long entryId) {
        this.entryId = entryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }
}
```


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoCollection;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;

import java.util.List;

/**
 * Full loaded entry object so we may examine replies.
 * This one contains BaseUserDTO (so we know who this entry belongs to if we
 * need to look up used record) and replies as BaseUserEntryDTO as we do not want
 * to load replies of replies until we need them.
 */
@Dto
public class UserEntryDTOImpl extends BaseUserEntryDTOImpl implements UserEntryDTO {

    @DtoField(dtoBeanKey = "BaseUserDTO", readOnly = true)
    private BaseUserDTO user;

    @DtoCollection(dtoBeanKey = "BaseUserEntryReplyDTO",
                   dtoToEntityMatcherKey = "BaseUserEntryDTOMatcher",
                   entityGenericType = UserEntryReply.class, readOnly = true)
    private List<BaseUserEntryDTO> replies;


    public BaseUserDTO getUser() {
        return user;
    }

    public void setUser(final BaseUserDTO user) {
        this.user = user;
    }

    public List<BaseUserEntryDTO> getReplies() {
        return replies;
    }

    public void setReplies(final List<BaseUserEntryDTO> replies) {
        this.replies = replies;
    }
}
```


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;

/**
 * The reply DTO object is in essence the same as base user entry,
 * so we can reuse the interface but provide a "carry over" mapping
 * for replyEntry property of domain entity.
 */
@Dto
public class BaseUserEntryReplyDTOImpl implements BaseUserEntryDTO {

    @DtoField(value = "replyEntry.entryId", readOnly = true)
    private Long entryId;
    @DtoField(value = "replyEntry.title", readOnly = true)
    private String title;
    @DtoField(value = "replyEntry.body", readOnly = true)
    private String body;

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(final Long entryId) {
        this.entryId = entryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }
}
```

We also need a matcher for collections:


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl.match;

import com.inspiresoftware.lib.dto.geda.adapter.DtoToEntityMatcher;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;

public class BaseUserEntryDTOMatcher implements DtoToEntityMatcher<BaseUserEntryDTO, Object> {

    public boolean match(final BaseUserEntryDTO baseUserEntryDTO, final Object o) {
        if (o instanceof UserEntry) {
            return baseUserEntryDTO.getEntryId().equalsUserEntry) o).getEntryId());
        } else if (o instanceof UserEntryReply) {
            return baseUserEntryDTO.getEntryId().equalsUserEntryReply) o).getReplyEntry().getEntryId());
        }
        throw new IllegalArgumentException("Invalid use");
    }
}
```

## DAO and Service layer

Now it is very important to have Service layer API that will allow us to achieve our goals. Here is what a sample implementation may look like:

**DAO**


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.service.impl;

import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {

    private final Map<Long, User> db = new HashMap<Long, User>();

    public User create(final String username) {
        final User user = new UserImpl();
        user.setUsername(username);
        db.put(user.getUserId(), user);
        return user;
    }

    public List<User> list() {
        return new ArrayList<User>(db.values());
    }

    public User findUser(final Long userId) {
        return db.get(userId);
    }

    public UserEntry findEntry(final Long entryId) {
        for (final User user : db.values()) {
            for (final UserEntry entry : user.getEntries()) {
                if (entry.getEntryId().equals(entryId)) {
                    return entry;
                }
            }
        }
        return null;
    }
}
```

**Service**


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.service.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.DTOAssembler;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private final UserDAO dao;
    private final BeanFactory bf;

    private final Map<String, Object> converters = new HashMap<String, Object>() {{

    }};

    public UserServiceImpl(final UserDAO dao, final BeanFactory bf) {
        this.dao = dao;
        this.bf = bf;
    }

    public List<BaseUserDTO> list() {
        final List<User> users = dao.list();
        final List<BaseUserDTO> dtos = new ArrayList<BaseUserDTO>();
        DTOAssembler.newAssembler(bf.get("BaseUserDTO").getClass(), bf.get("User").getClass())
                .assembleDtos(dtos, users, converters, bf);
        return dtos;
    }

    public List<UserDTO> list(final String filter) {
        final List<User> users = dao.list();
        final List<UserDTO> dtos = new ArrayList<UserDTO>();

        final DTOAssembler asm = DTOAssembler.newAssembler(bf.get(filter).getClass(), bf.get("User").getClass());

        for (final User user : users) {
            final UserDTO dto = (UserDTO) bf.get("UserDTO");
            asm.assembleDto(dto, user, converters, bf);
            dtos.add(dto);
        }
        return dtos;
    }

    public UserDTO findUser(final Long userId) {
        final User user = dao.findUser(userId);
        final UserDTO dto = (UserDTO) bf.get("UserDTO");
        DTOAssembler.newAssembler(dto.getClass(), bf.get("User").getClass())
                .assembleDto(dto, user, converters, bf);
        return dto;
    }

    public UserEntryDTO findEntry(final Long entryId) {
        final UserEntry entry = dao.findEntry(entryId);
        final UserEntryDTO dto = (UserEntryDTO) bf.get("UserEntryDTO");
        DTOAssembler.newAssembler(dto.getClass(), bf.get("UserEntry").getClass())
                .assembleDto(dto, entry, converters, bf);
        return dto;
    }
}
```

**BeanFactory for DTO and Entities**


```java


package com.inspiresoftware.lib.dto.geda.examples.blog.service.impl;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserEntryImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserEntryReplyImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.impl.UserImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.impl.*;

public class BlogBeanFactory implements BeanFactory {

    public Object get(final String entityBeanKey) {
        if ("BaseUserDTO".equals(entityBeanKey)) {
            return new BaseUserDTOImpl();
        } else if ("BaseUserEntryDTO".equals(entityBeanKey)) {
            return new BaseUserEntryDTOImpl();
        } else if ("BaseUserEntryReplyDTO".equals(entityBeanKey)) {
            return new BaseUserEntryReplyDTOImpl();
        } else if ("UserDTO".equals(entityBeanKey)) {
            return new UserDTOImpl();
        } else if ("UserEntryDTO".equals(entityBeanKey)) {
            return new UserEntryDTOImpl();
        }

        if ("User".equals(entityBeanKey)) {
            return new UserImpl();
        } else if ("UserEntry".equals(entityBeanKey)) {
            return new UserEntryImpl();
        } else if ("UserEntryReply".equals(entityBeanKey)) {
            return new UserEntryReplyImpl();
        }

        throw new IllegalArgumentException("No entry for : " + entityBeanKey);
    }
}
```

## Testing this API

The best way to see if all is well is by testing your API. So we are going to have a jUnit test that will simulate the user journey where we can achieve every objective that we have set in our goals:


```java


package com.inspiresoftware.lib.dto.geda.examples.blog;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.User;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntry;
import com.inspiresoftware.lib.dto.geda.examples.blog.domain.UserEntryReply;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.BaseUserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.dto.UserEntryDTO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserDAO;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.UserService;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.BlogBeanFactory;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.UserDAOImpl;
import com.inspiresoftware.lib.dto.geda.examples.blog.service.impl.UserServiceImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BlogExampleRun {

    private final UserDAO dao = new UserDAOImpl();
    private final BeanFactory bf = new BlogBeanFactory();
    private final UserService srv = new UserServiceImpl(dao, bf);

    @Test
    public void testBlogExample() {

        this.setupUsers();

        final List<BaseUserDTO> list = srv.list();
        assertNotNull(list);
        assertEquals(list.size(), 2);

        // imitate user found Bob's account in list
        final BaseUserDTO bob = selectBob(list);
        assertNotNull(bob);

        // user clicks the Bob's account entry
        final UserDTO bobFull = srv.findUser(bob.getUserId());
        assertNotNull(bobFull);

        assertNotNull(bobFull.getEntries());
        assertEquals(bobFull.getEntries().size(), 1);

        // user selects first entry
        final BaseUserEntryDTO bobEntry = bobFull.getEntries().get(0);
        assertNotNull(bobEntry);

        // user clicks bob's entry to see replies
        final UserEntryDTO bobEntryFull = srv.findEntry(bobEntry.getEntryId());
        assertNotNull(bobEntryFull);
        assertEquals(bobEntryFull.getTitle(), "GeDA");
        assertEquals(bobEntryFull.getBody(), "Hey all, This GeDA stuff really works!!!");
        assertNotNull(bobEntryFull.getReplies());
        assertEquals(bobEntryFull.getReplies().size(), 1);


        // user finds the reply
        final BaseUserEntryDTO replyToBob = bobEntryFull.getReplies().get(0);
        assertNotNull(replyToBob);
        assertEquals(replyToBob.getTitle(), "RE: GeDA");
        assertEquals(replyToBob.getBody(), "Awesome!");

        // user clicks the reply to see full details
        final UserEntryDTO replyToBobFull = srv.findEntry(replyToBob.getEntryId());
        assertNotNull(replyToBobFull);
        assertNotNull(replyToBobFull.getUser());
        assertEquals(replyToBobFull.getUser().getUsername(), "John");

        // and so on...

    }

    private BaseUserDTO selectBob(final List<BaseUserDTO> list) {
        for (final BaseUserDTO user : list) {
            if ("Bob".equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    private void setupUsers() {

        final User bob = dao.create("Bob");

        final UserEntry entry = bob.createEntry();
        entry.setTitle("GeDA");
        entry.setBody("Hey all, This GeDA stuff really works!!!");

        final User john = dao.create("John");
        final UserEntryReply reply = entry.createReply(john);
        reply.getReplyEntry().setBody("Awesome!");

    }

    @Test
    public void testBlogExampleListFilter() {

        this.setupUsers();

        // Here we load UserDTO but we will be using BaseUserDTO as
        // assembler class so we do not populate the object fully
        final List<UserDTO> list = srv.list("BaseUserDTO");
        assertNotNull(list);
        assertEquals(list.size(), 2);

    }
}
```

> Note how elegant the solution ended up if we used interfaces for our DTO's and Entities. Since our API stays clean and neat. All the dirty work is done via BeanFactory and DTOAssembler.
> owever this is not always the case in real world. So if you absolutely must use the same classes you can use filtering feature as described in the testBlogExampleListFilter() whereby we use assembler for Base* class to generate full DTOs. Therefore you keep your object types (if you absolutely must) but do not load extra data.
> This is however not recommended as you end up with blanks in object and you cannot determine if this is blank data or simply it has not been loaded.

> Filtering feature has been implemented OOTB in Spring3 integration module, so you should use that

This example is in the SVN and will be available from v 2.0.2

[< back to main](https://github.com/inspire-software/geda-genericdto/tree/master/wiki)
